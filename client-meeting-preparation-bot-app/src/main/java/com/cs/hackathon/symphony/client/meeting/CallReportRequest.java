package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;

import java.util.Map;

public class CallReportRequest {
    private final Map<String, TopicInformation> topicInformationMap;

    public CallReportRequest(Map<String, TopicInformation> topicInformationMap) {
        this.topicInformationMap = topicInformationMap;
    }

    public String getCollectedInformation() {
        StringBuilder messageBuilder = new StringBuilder();
        for (String key : topicInformationMap.keySet()) {
            messageBuilder.append(key);
            messageBuilder.append("\n");
            messageBuilder.append(topicInformationMap.get(key).collect());
            messageBuilder.append("\n");
        }
        return messageBuilder.toString();
    }

}
