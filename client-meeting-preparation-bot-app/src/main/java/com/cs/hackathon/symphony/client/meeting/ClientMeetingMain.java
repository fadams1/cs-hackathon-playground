package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.*;
import com.cs.hackathon.symphony.workflow.ExternalTaskClientBuilder;
import com.cs.hackathon.symphony.workflow.WorkflowEngine;
import com.cs.hackathon.symphony.workflow.WorkflowEngineBuilder;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;

import java.time.LocalDateTime;

public class ClientMeetingMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMeetingController.class);
    private static final boolean WFENGINE_CONNECTED = false;
    private static final String TOPIC_INITIATE_RM_CONVERSATION = "topic-initiate-rm-conversation";
    public static final String DEFAULT_BUSINESS_KEY = "cs-hf-new-client-meeting-process";

    public static void main(String[] args) throws InitException, AuthenticationException {
        if (WFENGINE_CONNECTED) {
            LOGGER.info("Connecting to workflow engine..");
            ExternalTaskClientBuilder clientBuilder = new ExternalTaskClientBuilder();
            ExternalTaskClient client = clientBuilder.getNewExternalTaskClient(WFENGINE_CONNECTED);
            clientBuilder.subscribeToTopic(TOPIC_INITIATE_RM_CONVERSATION,
                            client,
                            startProcessorCallback());
        } else {
            startProcessor(null);
        }
    }

    private static WorkflowEngine getWorkflowEngine(final boolean enabled, final String businessKey){
        //get workflow engine details here
        WorkflowEngine we = new WorkflowEngineBuilder().getNewWorkflowEngine(WFENGINE_CONNECTED, DEFAULT_BUSINESS_KEY);
        LOGGER.info("Process instance found: " + we.getInstance().getBusinessKey());
        return we;
    }

    private static ThrowingFunction<ExternalTask, ExternalTask> startProcessorCallback() {
        return ExternalTask -> {
            startProcessor(getWorkflowEngine(WFENGINE_CONNECTED, DEFAULT_BUSINESS_KEY));
            return ExternalTask;
        };
    }

    private static void startProcessor(WorkflowEngine workflowEngine) throws InitException, AuthenticationException {
        new ClientMeetingController(new SymphonyClientBuilder(), workflowEngine).notifyClientMeeting(
            new ClientMeetingEvent("noopur.n.jain@credit-suisse.com",
             "fay", LocalDateTime.now().plusDays(1))
        );
    }
}
