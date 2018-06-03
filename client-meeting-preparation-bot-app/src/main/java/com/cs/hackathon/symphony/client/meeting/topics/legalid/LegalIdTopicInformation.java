package com.cs.hackathon.symphony.client.meeting.topics.legalid;

import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import org.apache.commons.lang3.StringUtils;

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

    @Override
    public String collect() {
        return StringUtils.join(legalIdsToDiscuss, ",");
    }
}
