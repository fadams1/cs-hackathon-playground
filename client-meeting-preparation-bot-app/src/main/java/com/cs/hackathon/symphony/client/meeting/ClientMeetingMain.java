package com.cs.hackathon.symphony.client.meeting;

import camunda.model.ProcessInstance;
import com.cs.hackathon.symphony.ExternalTaskClientBuilder;
import com.cs.hackathon.symphony.ProcessInstanceClientBuilder;
import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.ThrowingFunction;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;

import java.time.LocalDateTime;

public class ClientMeetingMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientMeetingController.class);
    private static final boolean WFENGINE_CONNECTED = true;
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

    private static ProcessInstance getProcessInstance(final String businessKey){
        //try getting the processInstance here
        ProcessInstance i = new ProcessInstanceClientBuilder().getNewProcessInstanceClient(WFENGINE_CONNECTED)
                .getProcessInstanceByBusinessKey(businessKey);
        LOGGER.info("Process instance found: " + i.getBusinessKey());
        return i;
    }

    private static ThrowingFunction<ExternalTask, ExternalTask> startProcessorCallback() {
        return ExternalTask -> {
            startProcessor(getProcessInstance(DEFAULT_BUSINESS_KEY));
            return ExternalTask;
        };
    }

    private static void startProcessor(ProcessInstance processInstance) throws InitException, AuthenticationException {
        new ClientMeetingController(new SymphonyClientBuilder(), processInstance).notifyClientMeeting(
            new ClientMeetingEvent("marianne.celino@credit-suisse.com",
             "mcelino", LocalDateTime.now().plusDays(1))
        );
    }
}
