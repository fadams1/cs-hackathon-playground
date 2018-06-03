package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.ThrowingFunction;
import com.cs.hackathon.symphony.client.meeting.topics.TopicHandlerMap;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import com.cs.hackathon.symphony.client.meeting.topics.TopicRequestContainer;
import com.cs.hackathon.symphony.wrapper.MessageSender;
import nlp.model.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;
import org.symphonyoss.client.services.ChatListener;
import org.symphonyoss.symphony.clients.model.SymMessage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClientMeetingPreparationProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMeetingPreparationProcessor.class);
    private final TopicHandlerMap topicHandlerMap = new TopicHandlerMap();
    private final Function<ClientMeetingEvent, Map<String, TopicInformation>> clientMeetingPreparer;
    private final SymphonyClient symphonyClient;
    private final SymphonyClientBuilder symphonyClientBuilder;

    public ClientMeetingPreparationProcessor(SymphonyClientBuilder symphonyClientBuilder) throws InitException, AuthenticationException {
        this.symphonyClient = symphonyClientBuilder.getNewSymphonyClient();
        this.symphonyClientBuilder = symphonyClientBuilder;
        this.clientMeetingPreparer = notifyRmOfAppointmentAndCollectTopicsToDiscuss()
                .andThen(discussRequestedTopics())
                .andThen(discussRemainingTopics());
    }

    public Map<String, TopicInformation> collectTopicInformation(ClientMeetingEvent clientMeetingEvent) {
        return clientMeetingPreparer.apply(clientMeetingEvent);
    }

    private Function<Map<String, TopicInformation>, Map<String, TopicInformation>> discussRemainingTopics() {
        return collectedTopicInformaton -> collectedTopicInformaton;
    }

    private Function<TopicRequestContainer, Map<String, TopicInformation>> discussRequestedTopics() {
        return topicsToDiscuss -> topicsToDiscuss.getRequestedAction()
                .stream().map(callTopicHandler(topicsToDiscuss.getRmChat()))
                .collect(Collectors.toMap(TopicInformation::getTopicName, ti -> ti));
    }

    private Function<Action, TopicInformation> callTopicHandler(MessageSender rmChat) {
        return topicAction -> topicHandlerMap.getTopicHandler(topicAction.getAction()).collectTopicInformation(topicAction, rmChat);
    }

    private ThrowingFunction<ClientMeetingEvent, TopicRequestContainer> notifyRmOfAppointmentAndCollectTopicsToDiscuss() {
        return clientMeetingEvent -> {
            MessageSender initialChat = symphonyClientBuilder.getNewSymphonyChat(symphonyClient, clientMeetingEvent.getRmEmail());
            CountDownLatch messageWaiter = new CountDownLatch(1);

            Set<Action> collectedActions = new HashSet<>();
            ChatListener chatListener = new ChatListener() {
                @Override
                public void onChatMessage(SymMessage symMessage) {
                    System.out.println("Thank you for your response. " + symMessage.getMessageText());
                    Action action = new Action();
                    action.setAction("LegalId");
                    collectedActions.add(action);
                    messageWaiter.countDown();
                }
            };
            initialChat.addListener(chatListener);
            initialChat.sendMessage("Hello RM. You have a meeting with " + clientMeetingEvent.getClientId() + " at " + clientMeetingEvent.getClientMeetingTime(), false);
            initialChat.sendMessage("Do you need any assistance?", true);
            LOGGER.info("Waiting for user response....");
            messageWaiter.await();
            initialChat.removeListener(chatListener);

            return new TopicRequestContainer(collectedActions, symphonyClient, initialChat);
        };
    }

}
