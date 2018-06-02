package com.cs.hackathon.symphony.client.meeting;

import nlp.model.Action;

import java.util.Map;
import java.util.Set;
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


    private Function<Set<Action>, Map<String, TopicInformation>> discussRequestedTopics() {
        return topicsToDiscuss -> topicsToDiscuss.stream().map(callTopicHandler())
                .collect(Collectors.toMap(TopicInformation::getTopicName, ti -> ti));
    }

    private Function<Action, TopicInformation> callTopicHandler() {
        return topicAction -> topicHandlerMap.getTopicHandler(topicAction.getAction()).collectTopicInformation();
    }

    private Function<ClientMeetingEvent, Set<Action>> notifyRmOfAppointmentAndCollectTopicsToDiscuss() {
        throw new RuntimeException("not yet implemented!");
    }

}
