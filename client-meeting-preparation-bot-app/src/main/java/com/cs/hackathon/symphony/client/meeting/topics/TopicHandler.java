package com.cs.hackathon.symphony.client.meeting.topics;

import com.cs.hackathon.symphony.wrapper.MessageSender;
import nlp.model.Action;

public interface TopicHandler {
    TopicInformation collectTopicInformation(Action action, MessageSender rmChat);
    boolean isTopicRelevent();
}
