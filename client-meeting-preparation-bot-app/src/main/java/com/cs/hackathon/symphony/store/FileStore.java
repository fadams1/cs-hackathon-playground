package com.cs.hackathon.symphony.store;

import com.cs.hackathon.symphony.model.CallReportRequest;
import com.cs.hackathon.symphony.model.ClientMeetingEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.writeStringToFile;

public class FileStore implements MeetingRepository {
    private final ObjectMapper mapper;
    private final File meetingEvent;
    private final File callReport;

    public FileStore() {
        mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());

        meetingEvent = new File("meetingEvent.json");
        callReport = new File("callReport.json");
    }

    @Override
    public ClientMeetingEvent getClientMeetingEventByClient(String clientId) throws IOException {
        String contents = FileUtils.readFileToString(meetingEvent);
        return mapper.readValue(contents, ClientMeetingEvent.class);
    }

    @Override
    public CallReportRequest getCallReportRequestByClient(String clientId) throws IOException {
        String contents = FileUtils.readFileToString(callReport);
        return mapper.readValue(contents, CallReportRequest.class);
    }

    @Override
    public void saveCallReportRequest(CallReportRequest reportRequest) throws IOException {
        String request = mapper.writeValueAsString(reportRequest);
        writeStringToFile(callReport, request);

    }

    @Override
    public void saveMeetingEvent(ClientMeetingEvent event) throws IOException {
        String request = mapper.writeValueAsString(event);
        writeStringToFile(meetingEvent, request);
    }
}
