package com.cs.hackathon.symphony.client.meeting.topics.investment;

import com.cs.hackathon.symphony.client.meeting.topics.TopicHandler;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import nlp.model.Action;
import org.symphonyoss.client.model.Chat;

public class InvestmentTopicHandler implements TopicHandler {
    @Override
    public TopicInformation collectTopicInformation(Action action, Chat rmChat) {
        return null;
    }

    @Override
    public boolean isTopicRelevent() {
        return false;
    }
}
