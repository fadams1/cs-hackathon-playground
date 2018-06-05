package com.cs.hackathon.symphony.client.meeting.topics;

import com.cs.hackathon.symphony.client.meeting.topics.legalid.LegalIdMap;
import com.cs.hackathon.symphony.client.meeting.topics.legalid.LegalIdTopicHandler;

import java.util.*;

public class TopicHandlerMap {
    public static String INVESTMENT = "Investment";
    public static String LEGAL_ID = "LegalId";
    public static String DOCUMENT = "Documents";
    private final Map<String, TopicHandler> topicHandlerMap;

    public TopicHandlerMap() {
        // this needs to match camunda insertion order until we have topics as a sub process
        topicHandlerMap = new LinkedHashMap<>();
        topicHandlerMap.put(LEGAL_ID, new LegalIdTopicHandler(new LegalIdMap()));
//        topicHandlerMap.put(INVESTMENT, new InvestmentTopicHandler());
//        topicHandlerMap.put(DOCUMENT, new DocumentTopicHandler());
    }

    public TopicHandler getTopicHandler(String topic) {
        return topicHandlerMap.get(topic);
    }

    public Collection<TopicHandler> getAll() {
        return topicHandlerMap.values();
    }
}
