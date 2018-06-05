package com.cs.hackathon.symphony.store;

import com.cs.hackathon.symphony.model.CallReportRequest;
import com.cs.hackathon.symphony.model.ClientMeetingEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface MeetingRepository {
    ClientMeetingEvent getClientMeetingEventByClient(String clientId) throws IOException;
    CallReportRequest getCallReportRequestByClient(String clientId) throws IOException;

    void saveCallReportRequest(CallReportRequest reportRequest) throws IOException;
    void saveMeetingEvent(ClientMeetingEvent event) throws IOException;
}
