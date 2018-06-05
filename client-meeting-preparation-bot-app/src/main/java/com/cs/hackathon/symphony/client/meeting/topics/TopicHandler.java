package com.cs.hackathon.symphony.client.meeting.topics;

import com.cs.hackathon.symphony.WorkflowEngine;
import com.cs.hackathon.symphony.client.meeting.ClientMeetingEvent;
import com.cs.hackathon.symphony.wrapper.MessageSender;
import nlp.model.Action;
import org.symphonyoss.client.exceptions.MessagesException;

public interface TopicHandler {
    TopicInformation collectTopicInformation(ClientMeetingEvent clientMeetingEvent, Action action, MessageSender rmChat) throws MessagesException;
    boolean isTopicRelevent();
    void complete(WorkflowEngine workflowEngine);
    String getTopicName();
}
