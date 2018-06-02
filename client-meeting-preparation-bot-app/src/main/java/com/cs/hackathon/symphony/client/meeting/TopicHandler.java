package com.cs.hackathon.symphony.client.meeting;

import org.symphonyoss.client.model.Chat;

public interface TopicHandler {
    TopicInformation collectTopicInformation(Chat rmChat);
    boolean isTopicRelevent();
}
