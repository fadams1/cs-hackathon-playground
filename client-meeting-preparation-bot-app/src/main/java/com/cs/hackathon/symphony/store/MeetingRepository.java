package com.cs.hackathon.symphony.store;

import com.cs.hackathon.symphony.client.meeting.CallReportRequest;
import com.cs.hackathon.symphony.client.meeting.ClientMeetingEvent;

public interface MeetingRepository {
    ClientMeetingEvent getClientMeetingEventByClient(String clientId);
    CallReportRequest getCallReportRequestByClient(String clientId);

    void saveCallReportRequest(CallReportRequest reportRequest);
    void saveMeetingEvent(ClientMeetingEvent event);
}
