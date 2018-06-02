package com.cs.hackathon.symphony.client.meeting;

import nlp.model.Action;
import org.symphonyoss.client.model.Chat;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClientMeetingPreparationProcessor {
    private final TopicHandlerMap topicHandlerMap = new TopicHandlerMap();
    private final Function<ClientMeetingEvent, Map<String, TopicInformation>> clientMeetingPreparer;

    public ClientMeetingPreparationProcessor() {
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
        return topicAction -> topicHandlerMap.getTopicHandler(topicAction.getAction()).collectTopicInformation(rmChat);
    }

    private Function<ClientMeetingEvent, TopicRequestContainer> notifyRmOfAppointmentAndCollectTopicsToDiscuss() {
        throw new RuntimeException("not yet implemented!");
    }

}
