package com.cs.hackathon.symphony.client.meeting.topics.legalid;

import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;

import java.util.List;

public class LegalIdTopicInformation implements TopicInformation {
    private final List<LegalIdInformation> legalIdsToDiscuss;

    public LegalIdTopicInformation(List<LegalIdInformation> legalIdsToDiscuss) {
        this.legalIdsToDiscuss = legalIdsToDiscuss;
    }

    @Override
    public String getTopicName() {
        return "LegalId";
    }
}
