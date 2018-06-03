package com.cs.hackathon.symphony.client.meeting.topics.legalid;

import java.time.LocalDateTime;

public class LegalIdInformation {
    private final String legalIdType;
    private final LocalDateTime expiry;
    private final String documentImageUrl;

    public LegalIdInformation(String legalIdType, LocalDateTime expiry, String documentImageUrl) {
        this.legalIdType = legalIdType;
        this.expiry = expiry;
        this.documentImageUrl = documentImageUrl;
    }

    public String getLegalIdType() {
        return legalIdType;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public String getDocumentImageUrl() {
        return documentImageUrl;
    }
}
