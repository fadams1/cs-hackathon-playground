package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.ThrowingFunction;
import com.cs.hackathon.symphony.client.meeting.topics.TopicHandlerMap;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import com.cs.hackathon.symphony.client.meeting.topics.TopicRequestContainer;
import nlp.model.Action;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.SymphonyClientConfigID;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;
import org.symphonyoss.client.model.Chat;
import org.symphonyoss.symphony.clients.model.SymUser;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClientMeetingPreparationProcessor {
    private final TopicHandlerMap topicHandlerMap = new TopicHandlerMap();
    private final Function<ClientMeetingEvent, Map<String, TopicInformation>> clientMeetingPreparer;
    private final SymphonyClient symphonyClient;

    public ClientMeetingPreparationProcessor(SymphonyClientBuilder symphonyClientBuilder) throws InitException, AuthenticationException {
        this.symphonyClient = symphonyClientBuilder.getNewSymphonyClient();
        this.clientMeetingPreparer = notifyRmOfAppointmentAndCollectTopicsToDiscuss()
                .andThen(discussRequestedTopics())
                .andThen(discussRemainingTopics());
    }

    public Map<String, TopicInformation> collectTopicInformation(ClientMeetingEvent clientMeetingEvent) {
        return clientMeetingPreparer.apply(clientMeetingEvent);
    }

    private Function<Map<String, TopicInformation>, Map<String, TopicInformation>> discussRemainingTopics() {
        throw new RuntimeException("not yet implemented!");
    }


    private Function<TopicRequestContainer, Map<String, TopicInformation>> discussRequestedTopics() {
        return topicsToDiscuss -> topicsToDiscuss.getRequestedAction().stream().map(callTopicHandler(topicsToDiscuss.getRmChat()

        ))
                .collect(Collectors.toMap(TopicInformation::getTopicName, ti -> ti));
    }

    private Function<Action, TopicInformation> callTopicHandler(Chat rmChat) {
        return topicAction -> topicHandlerMap.getTopicHandler(topicAction.getAction()).collectTopicInformation(topicAction, rmChat);
    }

    private ThrowingFunction<ClientMeetingEvent, TopicRequestContainer> notifyRmOfAppointmentAndCollectTopicsToDiscuss() {
        return clientMeetingEvent -> {
            Chat initialChat = new Chat();
            initialChat.setLocalUser(symphonyClient.getLocalUser());

            Set<SymUser> remoteUsers = new HashSet<>();
            remoteUsers.add(symphonyClient.getUsersClient().getUserFromEmail(clientMeetingEvent.getRmEmail()));
            initialChat.setRemoteUsers(remoteUsers);
            initialChat.setStream(symphonyClient.getStreamsClient().getStream(remoteUsers));
            return new TopicRequestContainer(Collections.emptySet(), symphonyClient, initialChat);
        };
    }

}
