package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.client.meeting.topics.legalid.LegalIdTopicHandler;

import java.util.HashMap;
import java.util.Map;

public class TopicHandlerMap {
    public static String INVESTMENT = "Investment";
    public static String LEGAL_ID = "LegalId";
    private final Map<String, TopicHandler> topicHandlerMap;

    public TopicHandlerMap() {
        topicHandlerMap = new HashMap<>();
        topicHandlerMap.put(LEGAL_ID, new LegalIdTopicHandler());
        topicHandlerMap.put(INVESTMENT, new InvestmentTopicHandler());
    }

    public TopicHandler getTopicHandler(String topic) {
        return topicHandlerMap.get(topic);
    }

}
