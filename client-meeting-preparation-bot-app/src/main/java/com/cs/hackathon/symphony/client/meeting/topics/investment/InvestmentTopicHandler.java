package com.cs.hackathon.symphony.client.meeting.topics.investment;

import com.cs.hackathon.symphony.client.meeting.ClientMeetingEvent;
import com.cs.hackathon.symphony.client.meeting.topics.TopicHandler;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import com.cs.hackathon.symphony.wrapper.MessageSender;
import nlp.model.Action;

public class InvestmentTopicHandler implements TopicHandler {
    @Override
    public TopicInformation collectTopicInformation(ClientMeetingEvent clientMeetingEvent, Action action, MessageSender rmChat) {
        return null;
    }

    @Override
    public boolean isTopicRelevent() {
        return false;
    }
}
