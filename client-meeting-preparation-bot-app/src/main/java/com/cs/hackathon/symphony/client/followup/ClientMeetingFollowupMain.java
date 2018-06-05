package com.cs.hackathon.symphony.client.followup;

import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.model.CallReportRequest;
import com.cs.hackathon.symphony.model.ClientMeetingEvent;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import com.cs.hackathon.symphony.store.FileStore;
import com.cs.hackathon.symphony.store.MeetingRepository;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ClientMeetingFollowupMain {

    public static void main(String[] args) throws InitException, AuthenticationException, IOException {
        MeetingRepository repository = new FileStore();

        Map<String, TopicInformation> topicInformationMap = new HashMap<>();
        topicInformationMap.put("LegalId", new TopicInformation() {
            @Override
            public String getTopicName() {
                return "LegalId";
            }

            @Override
            public String collect() {
                return "legalId, legalId2, legalId3";
            }
        });
        ClientMeetingEvent meetingEvent = repository.getClientMeetingEventByClient("fay");
        ClientMeetingFollowupEvent event = new ClientMeetingFollowupEvent(meetingEvent, topicInformationMap);
        FollowupController controller = new FollowupController(new SymphonyClientBuilder());
        controller.processFollowUpEvent(event);

    }
}
