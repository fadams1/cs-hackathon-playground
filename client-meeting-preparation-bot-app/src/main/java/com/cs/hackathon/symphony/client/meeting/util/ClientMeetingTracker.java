package com.cs.hackathon.symphony.client.meeting.util;

import com.cs.hackathon.symphony.ThrowingFunction;
import com.cs.hackathon.symphony.workflow.ExternalTaskClientBuilder;
import com.cs.hackathon.symphony.workflow.WorkflowEngine;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;

public class ClientMeetingTracker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMeetingTracker.class);
    private static final String TOPIC_MEET_CLIENT = "topic-meet-client" ;

    private WorkflowEngine workflowEngine;

    public ClientMeetingTracker(WorkflowEngine workflowEngine) throws InitException, AuthenticationException {
            LOGGER.info("Starting call report logger subscription..");
            this.workflowEngine = workflowEngine;
            ExternalTaskClientBuilder clientBuilder = new ExternalTaskClientBuilder();
            ExternalTaskClient client = clientBuilder.getNewExternalTaskClient(Boolean.TRUE);
            clientBuilder.subscribeToTopic(TOPIC_MEET_CLIENT,
                    client,
                    trackClientMeetingCallback());
    }

    private static ThrowingFunction<ExternalTask, ExternalTask> trackClientMeetingCallback() {
        return ExternalTask -> {
            trackClientMeeting();
            return ExternalTask;
        };
    }

    private static void trackClientMeeting(){
        LOGGER.info("Client Meeting done.");
    }

}
