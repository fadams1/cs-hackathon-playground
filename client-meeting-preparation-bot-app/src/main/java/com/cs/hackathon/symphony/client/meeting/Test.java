package com.cs.hackathon.symphony.client.meeting;

import nlp.NLPConfig;
import nlp.NLPConfigLoader;
import nlp.NLPService;
import nlp.model.Action;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        NLPConfig nlpConfig = NLPConfigLoader.loadFromFile("/Users/noopurjain/work/cs-hackathon-playground/client-meeting-preparation-bot-app/src/main/resources/nlp-config.json");
        NLPService nlp = new NLPService(nlpConfig);
        List<Action> actions = nlp.match("I want to buy 10 apples.");
        System.out.println(actions.get(0).getAction());
    }

}
