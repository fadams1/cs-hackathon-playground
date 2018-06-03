package com.cs.hackathon.symphony;

import nlp.NLPConfig;
import nlp.NLPService;
import nlp.model.Action;

import java.nio.file.Paths;
import java.util.List;

public class ActionsFromMessageGetter {
    private static final String POS_MODEL = Paths.get("src/main/resources/en-pos-maxent.bin").toFile().getAbsolutePath();
    private static final String SENTENCE_MODEL = Paths.get("src/main/resources/en-sent.bin").toFile().getAbsolutePath();
    private static final String PATTERNS = Paths.get("src/main/resources/topicPatterns.json").toFile().getAbsolutePath();
    private final NLPService nlp;

    public ActionsFromMessageGetter() {
        NLPConfig nlpConfig = new NLPConfig();
        nlpConfig.setPatternsFile(PATTERNS);
        nlpConfig.setPOSModelPath(POS_MODEL);
        nlpConfig.setSentenceSplitterModelPath(SENTENCE_MODEL);
        nlp = new NLPService(nlpConfig);
    }

    public List<Action> getActions(String message) {
        return nlp.match(message);
    }
}
