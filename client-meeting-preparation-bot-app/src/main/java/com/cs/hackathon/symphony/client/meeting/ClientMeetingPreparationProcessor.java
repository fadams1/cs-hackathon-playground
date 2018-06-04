package com.cs.hackathon.symphony.client.meeting;

import camunda.TaskClient;
import camunda.model.ProcessInstance;
import camunda.model.Task;
import com.cs.hackathon.symphony.*;
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
    private final ProcessInstance processInstance;
    private final ActionsFromMessageGetter actionsFromMessageGetter = new ActionsFromMessageGetter();

    public ClientMeetingPreparationProcessor(SymphonyClientBuilder symphonyClientBuilder, ProcessInstance processInstance) throws InitException, AuthenticationException {
        this.symphonyClient = symphonyClientBuilder.getNewSymphonyClient();
        this.symphonyClientBuilder = symphonyClientBuilder;
        this.processInstance = processInstance;
        this.clientMeetingPreparer = notifyRmOfAppointmentAndCollectTopicsToDiscuss()
                .andThen(discussRequestedTopics())
                .andThen(discussRemainingTopics())
                .andThen(terminateChatWithRm());
    }

    private ThrowingFunction<TopicRequestContainer, Map<String, TopicInformation>> terminateChatWithRm() {
        return topicRequestContainer -> {
            MessageSender chat = topicRequestContainer.getRmChat();
            chat.sendMessage("Thank you, your meeting pack will be emailed to you shortly.", false);
            chat.sendMessage("Good bye and all the best.", false);
            return topicRequestContainer.getTopicInformationMap();
        };
    }

    public Map<String, TopicInformation> collectTopicInformation(ClientMeetingEvent clientMeetingEvent) {
        return clientMeetingPreparer.apply(clientMeetingEvent);
    }

    private Function<TopicRequestContainer, TopicRequestContainer> discussRemainingTopics() {
        return collectedTopicInformation -> collectedTopicInformation;
    }

    private Function<TopicRequestContainer, TopicRequestContainer> discussRequestedTopics() {
        return topicsToDiscuss -> {
            Map<String, TopicInformation> topicsMap = topicsToDiscuss.getRequestedAction()
                .stream().map(callTopicHandler(topicsToDiscuss.getClientMeetingEvent(), topicsToDiscuss.getRmChat()))
                .collect(Collectors.toMap(TopicInformation::getTopicName, ti -> ti));
            topicsToDiscuss.putAll(topicsMap);
            return topicsToDiscuss;
        };
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
                //complete task in worfklow
                completeTask(symphonyClient.getLocalUser().getEmailAddress());
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

    private void completeTask(String userEmail){
        if (processInstance == null) return;

        TaskClient client = new TaskClientBuilder().getNewTaskClient(true);
        Task task = client.getTask(processInstance.getId(), userEmail);
        client.completeTask(task.getId());
        LOGGER.info("Completed task " + task.getId() + " for user " + userEmail);
    }
}
