package com.cs.hackathon.symphony.model;

import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import java.util.Map;

public class CallReportRequest {
    @JsonSerialize(keyUsing = MapSerializer.class)
    @JsonProperty("topicInformationMap")
    private final Map<String, TopicInformation> topicInformationMap;

    @JsonProperty("callReportId")
    private final String callReportId;

    public CallReportRequest(@JsonProperty("topicInformationMap") Map<String, TopicInformation> topicInformationMap,
                             @JsonProperty("callReportId") String callReportId) {
        this.topicInformationMap = topicInformationMap;
        this.callReportId = callReportId;
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

    public String getCallReportId() {
        return callReportId;
    }

    public Map<String, TopicInformation> getTopicInformationMap() {
        return topicInformationMap;
    }
}
