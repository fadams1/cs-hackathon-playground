package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.ThrowingFunction;
import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.ThrowingFunction;
import com.cs.hackathon.symphony.model.CallReportRequest;
import com.cs.hackathon.symphony.model.ClientMeetingEvent;
import com.cs.hackathon.symphony.store.FileStore;
import com.cs.hackathon.symphony.store.MeetingRepository;
import com.cs.hackathon.symphony.workflow.ExternalTaskClientBuilder;
import com.cs.hackathon.symphony.workflow.WorkflowEngine;
import com.cs.hackathon.symphony.workflow.WorkflowEngineBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;

import java.io.IOException;
import java.time.LocalDateTime;

public class ClientMeetingMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMeetingController.class);
    private static final boolean WFENGINE_CONNECTED = false;
    private static final String TOPIC_INITIATE_RM_CONVERSATION = "topic-initiate-rm-conversation";
    public static final String DEFAULT_BUSINESS_KEY = "cs-hf-new-client-meeting-process";

    public static void main(String[] args) throws InitException, AuthenticationException, IOException {
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

    private static void startProcessor(WorkflowEngine workflowEngine) throws InitException, AuthenticationException, IOException {
        MeetingRepository repository = new FileStore();
        ClientMeetingEvent event = new ClientMeetingEvent("noopur.n.jain@credit-suisse.com",
                "fay", LocalDateTime.now().plusDays(1), "Starbucks, Kembangan");

        repository.saveMeetingEvent(event);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Jdk8Module()).registerModule(new JavaTimeModule());
        CallReportRequest request = new ClientMeetingController(
                new SymphonyClientBuilder(), workflowEngine)
                .notifyClientMeeting(
                event
        );
    }
}
