package com.cs.hackathon.symphony.client.meeting.util;

import com.cs.hackathon.symphony.*;
import com.cs.hackathon.symphony.workflow.ExternalTaskClientBuilder;
import com.cs.hackathon.symphony.workflow.WorkflowEngine;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;

public class EmailSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
    private static final String TOPIC_SEND_EMAIL = "topic-send-email" ;

    private WorkflowEngine workflowEngine;

    public EmailSender(WorkflowEngine workflowEngine, String email) throws InitException, AuthenticationException {
            LOGGER.info("Starting email sender subscription..");
            this.workflowEngine = workflowEngine;
            ExternalTaskClientBuilder clientBuilder = new ExternalTaskClientBuilder();
            ExternalTaskClient client = clientBuilder.getNewExternalTaskClient(Boolean.TRUE);
            clientBuilder.subscribeToTopic(TOPIC_SEND_EMAIL,
                    client,
                    sendEmailCallback(email));
    }

    private static ThrowingFunction<ExternalTask, ExternalTask> sendEmailCallback(String email) {
        return ExternalTask -> {
            sendEmail(email);
            return ExternalTask;
        };
    }

    private static void sendEmail(String email) throws InitException, AuthenticationException {
        LOGGER.info("Email sent to " + email);
    }

}
