package com.cs.hackathon.symphony.client.followup;

import com.cs.hackathon.symphony.SymphonyClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;

import java.util.function.Function;

public class FollowupController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FollowupController.class);
    private final Function<ClientMeetingFollowupEvent, CallReportTask> generatCallReportTask;
    private final FollowupProcessor followupProcessor;

    public FollowupController(SymphonyClientBuilder symphonyClientBuilder) throws InitException, AuthenticationException {
        followupProcessor = new FollowupProcessor(symphonyClientBuilder);
        generatCallReportTask = callReportTaskReceived();
    }

    public CallReportTask processFollowUpEvent(ClientMeetingFollowupEvent event) {
        return generatCallReportTask.apply(event);
    }

    private Function<ClientMeetingFollowupEvent, CallReportTask> callReportTaskReceived() {
        return followupProcessor::getCallReportTask;
    }
}
