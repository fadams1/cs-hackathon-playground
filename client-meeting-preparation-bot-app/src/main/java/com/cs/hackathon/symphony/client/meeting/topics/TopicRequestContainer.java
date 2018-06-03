package com.cs.hackathon.symphony.client.meeting.topics;

import com.cs.hackathon.symphony.client.meeting.ClientMeetingEvent;
import com.cs.hackathon.symphony.wrapper.MessageSender;
import nlp.model.Action;
import org.symphonyoss.client.SymphonyClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TopicRequestContainer {
    private final ClientMeetingEvent clientMeetingEvent;
    private final Set<Action> requestedAction;
    private final SymphonyClient client;
    private final MessageSender rmChat;
    private final Map<String, TopicInformation> topicInformationMap;

    public TopicRequestContainer(ClientMeetingEvent clientMeetingEvent, Set<Action> requestedAction, SymphonyClient client, MessageSender rmChat) {
        this.topicInformationMap = new HashMap<>();
        this.clientMeetingEvent = clientMeetingEvent;
        this.requestedAction = requestedAction;
        this.client = client;
        this.rmChat = rmChat;
    }

    public ClientMeetingEvent getClientMeetingEvent() {
        return clientMeetingEvent;
    }

    public Set<Action> getRequestedAction() {
        return requestedAction;
    }

    public MessageSender getRmChat() {
        return rmChat;
    }

    public SymphonyClient getClient() {
        return client;
    }

    public Map<String, TopicInformation> getTopicInformationMap() {
        return topicInformationMap;
    }

    public void addTopicInformation(String topic, TopicInformation information) {
        topicInformationMap.put(topic, information);
    }

    public void putAll(Map<String, TopicInformation> mapValues) {
        topicInformationMap.putAll(mapValues);
    }
}
