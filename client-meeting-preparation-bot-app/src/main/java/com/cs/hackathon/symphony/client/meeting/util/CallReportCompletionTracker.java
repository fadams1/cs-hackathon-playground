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

public class CallReportCompletionTracker {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallReportCompletionTracker.class);
    private static final String TOPIC_COMPLETE_CALL_REPORT = "topic-complete-call-report" ;

    private WorkflowEngine workflowEngine;

    public CallReportCompletionTracker(WorkflowEngine workflowEngine) throws InitException, AuthenticationException {
            LOGGER.info("Starting call report initiator subscription..");
            this.workflowEngine = workflowEngine;
            ExternalTaskClientBuilder clientBuilder = new ExternalTaskClientBuilder();
            ExternalTaskClient client = clientBuilder.getNewExternalTaskClient(Boolean.TRUE);
            clientBuilder.subscribeToTopic(TOPIC_COMPLETE_CALL_REPORT,
                    client,
                    callReportCompletionCallback());
    }

    private static ThrowingFunction<ExternalTask, ExternalTask> callReportCompletionCallback() {
        return ExternalTask -> {
            callReportComplete();
            return ExternalTask;
        };
    }

    private static void callReportComplete(){
        LOGGER.info("Call report completed. This completes the workflow. Goodbye!");
    }

}
