package com.cs.hackathon.symphony.client.meeting.topics.legalid;

import java.time.LocalDateTime;

public class LegalIdInformation {
    private final String legalIdType;
    private final LocalDateTime expiry;

    public LegalIdInformation(String legalIdType, LocalDateTime expiry) {
        this.legalIdType = legalIdType;
        this.expiry = expiry;
    }

    public String getLegalIdType() {
        return legalIdType;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }
}