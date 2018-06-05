package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.ActionsFromMessageGetter;
import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.ThrowingFunction;
import com.cs.hackathon.symphony.client.meeting.util.*;
import com.cs.hackathon.symphony.workflow.WorkflowEngine;
import com.cs.hackathon.symphony.client.meeting.init.RmConversationInitiator;
import com.cs.hackathon.symphony.client.meeting.topics.TopicHandler;
import com.cs.hackathon.symphony.client.meeting.topics.TopicHandlerMap;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import com.cs.hackathon.symphony.client.meeting.topics.TopicRequestContainer;
import com.cs.hackathon.symphony.client.meeting.util.CallReportLogger;
import com.cs.hackathon.symphony.client.meeting.util.EmailSender;
import com.cs.hackathon.symphony.model.ClientMeetingEvent;
import com.cs.hackathon.symphony.workflow.WorkflowEngine;
import com.cs.hackathon.symphony.wrapper.MessageSender;
import nlp.model.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;
import org.symphonyoss.client.services.ChatListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.function.Predicate;

public class ClientMeetingPreparationProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMeetingPreparationProcessor.class);
    private final TopicHandlerMap topicHandlerMap = new TopicHandlerMap();
    private final Function<ClientMeetingEvent, Map<String, TopicInformation>> clientMeetingPreparer;
    private final SymphonyClient symphonyClient;
    private final SymphonyClientBuilder symphonyClientBuilder;
    private final WorkflowEngine workflowEngine;
    private final ActionsFromMessageGetter actionsFromMessageGetter = new ActionsFromMessageGetter();

    public ClientMeetingPreparationProcessor(SymphonyClientBuilder symphonyClientBuilder, WorkflowEngine workflowEngine) throws InitException, AuthenticationException, InitException, AuthenticationException {
        this.symphonyClient = symphonyClientBuilder.getNewSymphonyClient();
        this.symphonyClientBuilder = symphonyClientBuilder;
        this.workflowEngine = workflowEngine;
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
            Collection<TopicHandler> possibleTopics = topicHandlerMap.getAll();
            final Map<String, TopicInformation> topicsMap = new HashMap<>();
            possibleTopics.forEach(topicHandler -> {
                if (topicsToDiscuss.getRequestedAction().stream().anyMatch(matchesAction(topicHandler))) {
                    TopicInformation result = callTopicHandler(topicsToDiscuss.getClientMeetingEvent(), topicsToDiscuss.getRmChat())
                            .apply(topicsToDiscuss.getRequestedAction().stream().filter(matchesAction(topicHandler)).findFirst().get());
                    topicsMap.put(result.getTopicName(), result);
                }
                topicHandler.complete(workflowEngine);
            });
            topicsToDiscuss.putAll(topicsMap);
            return topicsToDiscuss;
        };
    }

    private Predicate<Action> matchesAction(TopicHandler topicHandler) {
        return action -> action.getAction().equals(topicHandler.getTopicName());
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
                if (workflowEngine != null) {
                    workflowEngine.completeTask(symphonyClient.getLocalUser().getEmailAddress());
                }
                messageWaiter.countDown();
            };

            //register workflow subscriptions
            if (workflowEngine !=null) {
                registerSubscriptions(clientMeetingEvent);
            }

            initialChat.addListener(chatListener);
            initialChat.sendMessage(RmConversationInitiator.HELLO_RM.apply(clientMeetingEvent), false);
            initialChat.sendMessage(RmConversationInitiator.WHAT_DO_YOU_WANT_TO_DISCUSS_WITH_THE_CLIENT, false);
            LOGGER.info("Waiting for user response....");
            messageWaiter.await();
            initialChat.removeListener(chatListener);

            return new TopicRequestContainer(clientMeetingEvent, collectedActions, symphonyClient, initialChat);
        };
    }

    private void registerSubscriptions(ClientMeetingEvent clientMeetingEvent) throws InitException, AuthenticationException {
        if (workflowEngine == null) return;

        new EmailSender(workflowEngine, clientMeetingEvent.getRmEmail());
        new CallReportLogger(workflowEngine);
        new ClientMeetingTracker(workflowEngine);
        new CallReportInitiator(workflowEngine);
        new CallReportCompletionTracker(workflowEngine);
    }

}
