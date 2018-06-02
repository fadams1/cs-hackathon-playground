package com.cs.hackathon.symphony.client.meeting.topics;

import com.cs.hackathon.symphony.wrapper.MessageSender;
import nlp.model.Action;
import org.symphonyoss.client.SymphonyClient;

import java.util.Set;

public class TopicRequestContainer {
    private final Set<Action> requestedAction;
    private final SymphonyClient client;
    private final MessageSender rmChat;

    public TopicRequestContainer(Set<Action> requestedAction, SymphonyClient client, MessageSender rmChat) {
        this.requestedAction = requestedAction;
        this.client = client;
        this.rmChat = rmChat;
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
}
