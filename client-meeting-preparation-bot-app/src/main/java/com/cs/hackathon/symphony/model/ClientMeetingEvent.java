package com.cs.hackathon.symphony.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class ClientMeetingEvent {
    @JsonProperty("rmEmail")
    private final String rmEmail;
    @JsonProperty("clientId")
    private final String clientId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonProperty("clientMeetingTime")
    private final LocalDateTime clientMeetingTime;
    @JsonProperty("venue")
    private final String venue;

    @JsonCreator
    public ClientMeetingEvent(@JsonProperty("rmEmail") String rmEmail,
                              @JsonProperty("clientId") String clientId,
                              @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
                              @JsonProperty("clientMeetingTime") LocalDateTime clientMeetingTime,
                              @JsonProperty("venue") String venue) {
        this.rmEmail = rmEmail;
        this.clientId = clientId;
        this.clientMeetingTime = clientMeetingTime;
        this.venue = venue;
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
}
