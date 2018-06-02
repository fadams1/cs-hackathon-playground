package com.cs.hackathon.symphony.client.meeting.topics;

import nlp.model.Action;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.model.Chat;

import java.util.Set;

public class TopicRequestContainer {
    private final Set<Action> requestedAction;
    private final SymphonyClient client;
    private final Chat rmChat;

    public TopicRequestContainer(Set<Action> requestedAction, SymphonyClient client, Chat rmChat) {
        this.requestedAction = requestedAction;
        this.client = client;
        this.rmChat = rmChat;
    }

    public Set<Action> getRequestedAction() {
        return requestedAction;
    }

    public Chat getRmChat() {
        return rmChat;
    }

    public SymphonyClient getClient() {
        return client;
    }
}
