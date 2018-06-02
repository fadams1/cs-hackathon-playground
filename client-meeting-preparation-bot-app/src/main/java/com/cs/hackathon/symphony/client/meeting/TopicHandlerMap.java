package com.cs.hackathon.symphony.client.meeting;

import java.util.HashMap;
import java.util.Map;

public class TopicHandlerMap {
    public static String KYC = "KYC";
    public static String INVESTMENT = "Investment";
    public static String DOCUMENTS = "Documents";
    private final Map<String, TopicHandler> topicHandlerMap;

    public TopicHandlerMap() {
        topicHandlerMap = new HashMap<>();
        topicHandlerMap.put(KYC, new KYCTopicHandler());
        topicHandlerMap.put(INVESTMENT, new InvestmentTopicHandler());
        topicHandlerMap.put(DOCUMENTS, new DocumentTopicHandler());
    }

    public TopicHandler getTopicHandler(String topic) {
        return topicHandlerMap.get(topic);
    }

}
