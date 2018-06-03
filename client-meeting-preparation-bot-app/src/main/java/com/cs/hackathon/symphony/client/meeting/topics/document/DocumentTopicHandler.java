package com.cs.hackathon.symphony.client.meeting.topics.document;

import com.cs.hackathon.symphony.client.meeting.ClientMeetingEvent;
import com.cs.hackathon.symphony.client.meeting.topics.TopicHandler;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import com.cs.hackathon.symphony.wrapper.MessageSender;
import nlp.model.Action;
import org.symphonyoss.client.exceptions.MessagesException;
import org.symphonyoss.client.model.Chat;

public class DocumentTopicHandler implements TopicHandler {

    @Override
    public TopicInformation collectTopicInformation(ClientMeetingEvent clientMeetingEvent, Action action, MessageSender rmChat) throws MessagesException {
        return new TopicInformation() {
            @Override
            public String getTopicName() {
                return "Documents";
            }

            @Override
            public String collect() {
                return "Document1, Document2, Document3";
            }
        };
    }

    @Override
    public boolean isTopicRelevent() {
        return false;
    }
}
