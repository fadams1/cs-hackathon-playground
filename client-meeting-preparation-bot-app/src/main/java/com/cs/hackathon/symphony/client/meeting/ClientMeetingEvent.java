package com.cs.hackathon.symphony.client.meeting;

import java.time.LocalDateTime;

public class ClientMeetingEvent {
    private final String rmEmail;
    private final String clientId;
    private final LocalDateTime clientMeetingTime;

    public ClientMeetingEvent(String rmEmail, String clientId, LocalDateTime clientMeetingTime) {
        this.rmEmail = rmEmail;
        this.clientId = clientId;
        this.clientMeetingTime = clientMeetingTime;
    }

    public String getRmEmail() {
        return rmEmail;
    }

    public String getClientId() {
        return clientId;
    }

    public LocalDateTime getClientMeetingTime() {
        return clientMeetingTime;
    }

    @Override
    public String toString() {
        return "ClientMeetingEvent{" +
                "rmEmail='" + rmEmail + '\'' +
                ", clientId='" + clientId + '\'' +
                ", clientMeetingTime=" + clientMeetingTime +
                '}';
    }
}
