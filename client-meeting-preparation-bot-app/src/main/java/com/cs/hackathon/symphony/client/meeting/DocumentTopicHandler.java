package com.cs.hackathon.symphony.client.meeting;

import org.symphonyoss.client.model.Chat;

public class DocumentTopicHandler implements TopicHandler {
    @Override
    public TopicInformation collectTopicInformation(Chat rmChat) {
        return null;
    }

    @Override
    public boolean isTopicRelevent() {
        return false;
    }
}
