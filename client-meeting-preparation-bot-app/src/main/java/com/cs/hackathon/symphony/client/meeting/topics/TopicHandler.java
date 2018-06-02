package com.cs.hackathon.symphony.client.meeting.topics;

import nlp.model.Action;
import org.symphonyoss.client.model.Chat;

public interface TopicHandler {
    TopicInformation collectTopicInformation(Action action, Chat rmChat);
    boolean isTopicRelevent();
}
