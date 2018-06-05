package com.cs.hackathon.symphony.client.followup;

import com.cs.hackathon.symphony.wrapper.MessageSender;
import nlp.model.Action;
import org.symphonyoss.client.SymphonyClient;

public class FollowupRequestContainer {
    private final MessageSender rmChat;
    private final SymphonyClient symphonyClient;
    private final Action action;
    private final ClientMeetingFollowupEvent followupEvent;

    public FollowupRequestContainer(MessageSender rmChat, SymphonyClient symphonyClient, Action action, ClientMeetingFollowupEvent followupEvent) {
        this.rmChat = rmChat;
        this.symphonyClient = symphonyClient;
        this.action = action;
        this.followupEvent = followupEvent;
    }

    public Action getAction() {
        return action;
    }

    public SymphonyClient getSymphonyClient() {
        return symphonyClient;
    }

    public MessageSender getRmChat() {
        return rmChat;
    }

    public ClientMeetingFollowupEvent getFollowupEvent() {
        return followupEvent;
    }
}
