package com.cs.hackathon.symphony.client.meeting;

public interface TopicHandler {
    TopicInformation collectTopicInformation();
    boolean isTopicRelevent();
}
