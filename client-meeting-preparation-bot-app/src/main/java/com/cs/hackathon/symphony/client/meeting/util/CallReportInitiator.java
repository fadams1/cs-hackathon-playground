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

public class CallReportInitiator {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallReportInitiator.class);
    private static final String TOPIC_INITIATE_CALL_REPORT = "topic-initiate-call-report" ;

    private WorkflowEngine workflowEngine;

    public CallReportInitiator(WorkflowEngine workflowEngine) throws InitException, AuthenticationException {
            LOGGER.info("Starting call report initiator subscription..");
            this.workflowEngine = workflowEngine;
            ExternalTaskClientBuilder clientBuilder = new ExternalTaskClientBuilder();
            ExternalTaskClient client = clientBuilder.getNewExternalTaskClient(Boolean.TRUE);
            clientBuilder.subscribeToTopic(TOPIC_INITIATE_CALL_REPORT,
                    client,
                    callReportInitiatorCallback());
    }

    private static ThrowingFunction<ExternalTask, ExternalTask> callReportInitiatorCallback() {
        return ExternalTask -> {
            callReportInitiator();
            return ExternalTask;
        };
    }

    private static void callReportInitiator(){
        LOGGER.info("Initiating call report population.");
    }

}
