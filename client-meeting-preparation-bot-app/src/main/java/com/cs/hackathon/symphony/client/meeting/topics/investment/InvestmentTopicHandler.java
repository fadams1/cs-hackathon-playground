package com.cs.hackathon.symphony.client.meeting.topics.investment;

import com.cs.hackathon.symphony.WorkflowEngine;
import com.cs.hackathon.symphony.client.meeting.ClientMeetingEvent;
import com.cs.hackathon.symphony.client.meeting.topics.TopicHandler;
import com.cs.hackathon.symphony.client.meeting.topics.TopicHandlerMap;
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

    @Override
    public void complete(WorkflowEngine engine) {
        if (engine != null) {
            engine.completeTask("bot.user41@example.com");
        }
    }

    @Override
    public String getTopicName() {
        return TopicHandlerMap.INVESTMENT;
    }
}

