package com.cs.hackathon.symphony.client.meeting;

import nlp.NLPConfig;
import nlp.NLPConfigLoader;
import nlp.NLPService;
import nlp.model.Action;

import java.util.List;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        NLPConfig nlpConfig = NLPConfigLoader.loadFromFile("/Users/noopurjain/work/cs-hackathon-playground/client-meeting-preparation-bot-app/src/main/resources/nlp-config.json");
        NLPService nlp = new NLPService(nlpConfig);
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String message = scanner.nextLine();
            List<Action> actions = nlp.match(message.toLowerCase());
            for (Action action : actions) {
                System.out.println(action.getAction());
                System.out.println(action.getParameters());
            }
        }
    }

}
