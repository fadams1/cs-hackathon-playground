package com.cs.hackathon.symphony.client.meeting;

import java.util.Map;
import java.util.function.Function;

public class ClientMeetingController {
    private final Function<ClientMeetingEvent, CallReportRequest> collectCallReportFromRm;
    private final ClientMeetingPreparationProcessor clientMeetingPreparationProcessor;

    public ClientMeetingController() {
        collectCallReportFromRm = clientMeetingEventReceived()
            .andThen(initiateClientPreparationCollection())
            .andThen(handleClientPreparationResponse());
        clientMeetingPreparationProcessor = new ClientMeetingPreparationProcessor();
    }

    private Function<Map<String, TopicInformation>, CallReportRequest> handleClientPreparationResponse() {
        return something -> new CallReportRequest();
    }

    private Function<ClientMeetingEvent, Map<String, TopicInformation>> initiateClientPreparationCollection() {
        return clientMeetingPreparationProcessor::collectTopicInformation;
    }

    private Function<ClientMeetingEvent, ClientMeetingEvent> clientMeetingEventReceived() {
        throw new RuntimeException("not yet implemented!");
    }

}
