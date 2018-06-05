package com.cs.hackathon.symphony;

import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;

import java.util.function.Function;

public class ExternalTaskClientBuilder {

    public static final String WFENGINE_REST = "http://localhost:8080/engine-rest";

    public ExternalTaskClient getNewExternalTaskClient(final boolean enabled){
        if (enabled){
            ExternalTaskClient client=ExternalTaskClient.create()
            .baseUrl(WFENGINE_REST)
            .build();
            return client;
        }
        return null;
    }

    public void subscribeToTopic(final String topicName, ExternalTaskClient client, Function<ExternalTask, ExternalTask> callback) {
        // subscribe to an external task topic as specified in the process
        client.subscribe(topicName)
        //.lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
        .handler((externalTask, externalTaskService) -> {
            // Complete the task
            externalTaskService.complete(externalTask);
            callback.apply(externalTask);
        })
        .open();
    }
}
