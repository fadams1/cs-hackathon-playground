package com.cs.hackathon.symphony.client.meeting.topics.legalid;

import com.cs.hackathon.symphony.client.meeting.topics.TopicHandler;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import nlp.model.Action;
import org.symphonyoss.client.model.Chat;

public class LegalIdTopicHandler implements TopicHandler {
    @Override
    public TopicInformation collectTopicInformation(Action action, Chat rmChat) {

//        {"action":"checkLegalId",
//                "posrequirementList":[
//            {"options":["passport","employment pass","EP","legal id"],"parameter":"legalIdTypes","pos":"NN"}]
//        },

        return null;
    }

    @Override
    public boolean isTopicRelevent() {
        return false;
    }
}
