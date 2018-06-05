package com.cs.hackathon.symphony.client.followup;

import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.client.meeting.ClientMeetingEvent;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import com.cs.hackathon.symphony.client.meeting.topics.legalid.LegalIdInformation;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ClientMeetingFollowupMain {

    public static void main(String[] args) throws InitException, AuthenticationException {
        ClientMeetingEvent meetingEvent = new ClientMeetingEvent("noopur.n.jain@credit-suisse.com",
                "fay", LocalDateTime.now().plusDays(1));
        Map<String, TopicInformation> topicInformationMap = new HashMap<>();
        topicInformationMap.put("legalId", new TopicInformation() {
            @Override
            public String getTopicName() {
                return "LegalId";
            }

            @Override
            public String collect() {
                return "LegalId";
            }
        });
        ClientMeetingFollowupEvent event = new ClientMeetingFollowupEvent(meetingEvent, topicInformationMap);
        FollowupController controller = new FollowupController(new SymphonyClientBuilder());
        controller.processFollowUpEvent(event);

    }
}
