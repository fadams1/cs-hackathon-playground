package com.cs.hackathon.symphony.client.followup;

import com.cs.hackathon.symphony.client.meeting.ClientMeetingEvent;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;

import java.util.Map;

public class ClientMeetingFollowupEvent {
    private final ClientMeetingEvent clientMeetingEvent;
    private final Map<String, TopicInformation> topicInformationMap;

    public ClientMeetingFollowupEvent(ClientMeetingEvent clientMeetingEvent, Map<String, TopicInformation> topicInformationMap) {
        this.clientMeetingEvent = clientMeetingEvent;
        this.topicInformationMap = topicInformationMap;
    }

    public ClientMeetingEvent getClientMeetingEvent() {
        return clientMeetingEvent;
    }
}
