package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.ActionsFromMessageGetter;
import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.ThrowingFunction;
import com.cs.hackathon.symphony.client.meeting.init.RmConversationInitiator;
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

import java.util.HashSet;
import java.util.List;
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
    private final ActionsFromMessageGetter actionsFromMessageGetter = new ActionsFromMessageGetter();

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
                .stream().map(callTopicHandler(topicsToDiscuss.getClientMeetingEvent(), topicsToDiscuss.getRmChat()))
                .collect(Collectors.toMap(TopicInformation::getTopicName, ti -> ti));
    }

    private ThrowingFunction<Action, TopicInformation> callTopicHandler(ClientMeetingEvent clientMeetingEvent, MessageSender rmChat) {
        return topicAction -> topicHandlerMap.getTopicHandler(topicAction.getAction()).collectTopicInformation(clientMeetingEvent, topicAction, rmChat);
    }

    private ThrowingFunction<ClientMeetingEvent, TopicRequestContainer> notifyRmOfAppointmentAndCollectTopicsToDiscuss() {
        return clientMeetingEvent -> {
            MessageSender initialChat = symphonyClientBuilder.getNewSymphonyChat(symphonyClient, clientMeetingEvent.getRmEmail());
            CountDownLatch messageWaiter = new CountDownLatch(1);

            Set<Action> collectedActions = new HashSet<>();
            ChatListener chatListener = symMessage -> {
                System.out.println("Thank you for your response. " + symMessage.getMessageText());
                List<Action> actions = actionsFromMessageGetter.getActions(symMessage.getMessageText());
                collectedActions.addAll(actions);
                messageWaiter.countDown();
            };

            initialChat.addListener(chatListener);
            initialChat.sendMessage(RmConversationInitiator.HELLO_RM.apply(clientMeetingEvent), false);
            initialChat.sendMessage(RmConversationInitiator.WHAT_DO_YOU_WANT_TO_DISCUSS_WITH_THE_CLIENT, false);
            LOGGER.info("Waiting for user response....");
            messageWaiter.await();
            initialChat.removeListener(chatListener);

            return new TopicRequestContainer(clientMeetingEvent, collectedActions, symphonyClient, initialChat);
        };
    }

}
