package com.cs.hackathon.symphony.client.meeting.util;

import com.cs.hackathon.symphony.workflow.ExternalTaskClientBuilder;
import com.cs.hackathon.symphony.ThrowingFunction;
import com.cs.hackathon.symphony.workflow.WorkflowEngine;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;

public class CallReportLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(CallReportLogger.class);
    private static final String TOPIC_SAVE_CALL_REPORT = "topic-save-call-report" ;

    private WorkflowEngine workflowEngine;

    public CallReportLogger(WorkflowEngine workflowEngine) throws InitException, AuthenticationException {
            LOGGER.info("Starting call report logger subscription..");
            this.workflowEngine = workflowEngine;
            ExternalTaskClientBuilder clientBuilder = new ExternalTaskClientBuilder();
            ExternalTaskClient client = clientBuilder.getNewExternalTaskClient(Boolean.TRUE);
            clientBuilder.subscribeToTopic(TOPIC_SAVE_CALL_REPORT,
                    client,
                    saveCallReportCallback());
    }

    private static ThrowingFunction<ExternalTask, ExternalTask> saveCallReportCallback() {
        return ExternalTask -> {
            saveCallReport();
            return ExternalTask;
        };
    }

    private static void saveCallReport(){
        LOGGER.info("Call report saved with status: " + "PENDING");
    }

}
