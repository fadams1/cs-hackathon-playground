package com.cs.hackathon.symphony;

import camunda.CamundaClient;
import camunda.CamundaConfigLoader;
import camunda.ProcessInstanceClient;
import camunda.model.*;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTask;

import java.util.function.Function;

public class ProcessInstanceClientBuilder {

    public ProcessInstanceClientProxy getNewProcessInstanceClient(final boolean enabled){
        if (enabled){
            CamundaConfig config = CamundaConfigLoader.loadFromFile("src/main/resources/camunda-config.json");
            return new ProcessInstanceClientProxy(config.getCamundaURL());

        }
        return null;
    }

    public void startProcessInstance(){
        //do we want to start the process
    }

}
