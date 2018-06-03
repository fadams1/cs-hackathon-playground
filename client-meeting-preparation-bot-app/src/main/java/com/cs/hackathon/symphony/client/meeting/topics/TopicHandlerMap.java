package com.cs.hackathon.symphony.client.meeting.topics;

import com.cs.hackathon.symphony.client.meeting.topics.document.DocumentTopicHandler;
import com.cs.hackathon.symphony.client.meeting.topics.investment.InvestmentTopicHandler;
import com.cs.hackathon.symphony.client.meeting.topics.legalid.LegalIdMap;
import com.cs.hackathon.symphony.client.meeting.topics.legalid.LegalIdTopicHandler;

import java.util.HashMap;
import java.util.Map;

public class TopicHandlerMap {
    public static String INVESTMENT = "Investment";
    public static String LEGAL_ID = "LegalId";
    public static String DOCUMENT = "Documents";
    private final Map<String, TopicHandler> topicHandlerMap;

    public TopicHandlerMap() {
        topicHandlerMap = new HashMap<>();
        topicHandlerMap.put(LEGAL_ID, new LegalIdTopicHandler(new LegalIdMap()));
        topicHandlerMap.put(INVESTMENT, new InvestmentTopicHandler());
        topicHandlerMap.put(DOCUMENT, new DocumentTopicHandler());
    }

    public TopicHandler getTopicHandler(String topic) {
        return topicHandlerMap.get(topic);
    }

}
