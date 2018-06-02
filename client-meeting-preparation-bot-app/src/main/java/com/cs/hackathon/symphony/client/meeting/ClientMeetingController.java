package com.cs.hackathon.symphony.client.meeting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;

public class ClientMeetingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMeetingController.class);
    private final Function<ClientMeetingEvent, CallReportRequest> collectCallReportFromRm;
    private final ClientMeetingPreparationProcessor clientMeetingPreparationProcessor;

    public ClientMeetingController() {
        collectCallReportFromRm = clientMeetingEventReceived()
            .andThen(initiateClientPreparationCollection())
            .andThen(handleClientPreparationResponse());
        clientMeetingPreparationProcessor = new ClientMeetingPreparationProcessor();
    }

    public void notifyClientMeeting(ClientMeetingEvent clientMeetingEvent) {
        collectCallReportFromRm.apply(clientMeetingEvent);
    }

    private Function<Map<String, TopicInformation>, CallReportRequest> handleClientPreparationResponse() {
        return something -> new CallReportRequest();
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
