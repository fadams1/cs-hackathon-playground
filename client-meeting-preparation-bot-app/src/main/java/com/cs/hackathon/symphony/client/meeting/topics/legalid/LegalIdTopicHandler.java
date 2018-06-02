package com.cs.hackathon.symphony.client.meeting.topics.legalid;

import com.cs.hackathon.symphony.client.meeting.topics.TopicHandler;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import com.cs.hackathon.symphony.wrapper.MessageSender;
import nlp.model.Action;

public class LegalIdTopicHandler implements TopicHandler {
    @Override
    public TopicInformation collectTopicInformation(Action action, MessageSender rmChat) {

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
