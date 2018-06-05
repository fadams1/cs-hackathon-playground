package com.cs.hackathon.symphony.workflow;

import camunda.CamundaConfigLoader;
import camunda.ProcessInstanceClient;
import camunda.TaskClient;
import camunda.model.CamundaConfig;
import camunda.model.Task;

public class TaskClientBuilder {

    public TaskClient getNewTaskClient(final boolean enabled){
        if (enabled){
            CamundaConfig config = CamundaConfigLoader.loadFromFile("src/main/resources/camunda-config.json");
            TaskClient client = new TaskClient(config.getCamundaURL());
            return client;
        }
        return null;
    }


}
