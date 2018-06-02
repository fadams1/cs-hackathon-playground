package com.cs.hackathon.symphony.client.meeting.topics.legalid;

import com.cs.hackathon.symphony.client.meeting.TopicHandler;
import com.cs.hackathon.symphony.client.meeting.TopicInformation;
import nlp.model.Action;
import org.symphonyoss.client.model.Chat;

public class LegalIdTopicHandler implements TopicHandler {
    @Override
    public TopicInformation collectTopicInformation(Action action, Chat rmChat) {
        return null;
    }

    @Override
    public boolean isTopicRelevent() {
        return false;
    }
}
