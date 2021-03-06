package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.workflow.WorkflowEngine;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import com.cs.hackathon.symphony.model.CallReportRequest;
import com.cs.hackathon.symphony.model.ClientMeetingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;

import java.util.Map;
import java.util.function.Function;

public class ClientMeetingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMeetingController.class);
    private final Function<ClientMeetingEvent, CallReportRequest> collectCallReportFromRm;
    private final ClientMeetingPreparationProcessor clientMeetingPreparationProcessor;

    public ClientMeetingController(SymphonyClientBuilder symphonyClientBuilder, WorkflowEngine workflowEngine) throws InitException, AuthenticationException {
        clientMeetingPreparationProcessor = new ClientMeetingPreparationProcessor(symphonyClientBuilder, workflowEngine);
        collectCallReportFromRm = clientMeetingEventReceived()
            .andThen(initiateClientPreparationCollection())
            .andThen(handleClientPreparationResponse());
    }

    public CallReportRequest notifyClientMeeting(ClientMeetingEvent clientMeetingEvent) {
        return collectCallReportFromRm.apply(clientMeetingEvent);
    }

    private Function<Map<String, TopicInformation>, CallReportRequest> handleClientPreparationResponse() {
        return map -> new CallReportRequest(map, "report1");
    }

    private Function<ClientMeetingEvent, Map<String, TopicInformation>> initiateClientPreparationCollection() {
        return clientMeetingPreparationProcessor::collectTopicInformation;
    }

    private Function<ClientMeetingEvent, ClientMeetingEvent> clientMeetingEventReceived() {
        return clientMeetingEvent -> {
            LOGGER.info("Received client meeting notification: {}", clientMeetingEvent);
            return clientMeetingEvent;
        };
    }

}
