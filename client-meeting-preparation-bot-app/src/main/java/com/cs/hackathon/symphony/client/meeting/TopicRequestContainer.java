package com.cs.hackathon.symphony.client.meeting;

import nlp.model.Action;
import org.symphonyoss.client.model.Chat;

import java.util.Set;

public class TopicRequestContainer {
    private final Set<Action> requestedAction;
    private final Chat rmChat;

    public TopicRequestContainer(Set<Action> requestedAction, Chat rmChat) {
        this.requestedAction = requestedAction;
        this.rmChat = rmChat;
    }

    public Set<Action> getRequestedAction() {
        return requestedAction;
    }

    public Chat getRmChat() {
        return rmChat;
    }
}
