package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.ExternalTaskClientBuilder;
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
    private static final boolean WFENGINE_CONNECTED = false;
    private static final String TOPIC_INITIATE_RM_CONVERSATION = "topic-initiate-rm-conversation";

    public static void main(String[] args) throws InitException, AuthenticationException {
        if (WFENGINE_CONNECTED) {
            LOGGER.info("Connecting to workflow engine..");
            ExternalTaskClientBuilder clientBuilder = new ExternalTaskClientBuilder();
            ExternalTaskClient client = clientBuilder.getNewExternalTaskClient(WFENGINE_CONNECTED);
            clientBuilder.subscribeToTopic(TOPIC_INITIATE_RM_CONVERSATION,
                            client,
                            startProcessorCallback());
        } else {
            startProcessor();
        }
    }

    private static ThrowingFunction<ExternalTask, ExternalTask> startProcessorCallback() {
        return ExternalTask -> {
            startProcessor();
            return ExternalTask;
        };
    }

    private static void startProcessor() throws InitException, AuthenticationException {
        new ClientMeetingController(new SymphonyClientBuilder()).notifyClientMeeting(
            new ClientMeetingEvent("fay.adams@credit-suisse.com",
             "fay", LocalDateTime.now().plusDays(1))
        );
    }
}
