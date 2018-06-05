package com.cs.hackathon.symphony.store;

import com.cs.hackathon.symphony.client.meeting.CallReportRequest;
import com.cs.hackathon.symphony.client.meeting.ClientMeetingEvent;

public class FileStore implements MeetingRepository {

    @Override
    public ClientMeetingEvent getClientMeetingEventByClient(String clientId) {
        return null;
    }

    @Override
    public CallReportRequest getCallReportRequestByClient(String clientId) {
        return null;
    }

    @Override
    public void saveCallReportRequest(CallReportRequest reportRequest) {

    }

    @Override
    public void saveMeetingEvent(ClientMeetingEvent event) {

    }
}
