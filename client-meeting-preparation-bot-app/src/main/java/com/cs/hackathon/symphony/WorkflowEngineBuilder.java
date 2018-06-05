package com.cs.hackathon.symphony;

import camunda.CamundaConfigLoader;
import camunda.model.CamundaConfig;

public class WorkflowEngineBuilder {

    public WorkflowEngine getNewWorkflowEngine(final boolean enabled, final String businessKey){
        if (enabled){
            CamundaConfig config = CamundaConfigLoader.loadFromFile("src/main/resources/camunda-config.json");
            WorkflowEngineRestClient wfClient = new WorkflowEngineRestClient(config.getCamundaURL());
            WorkflowEngine engine = new WorkflowEngine(wfClient.getProcessEngineName(),
                    wfClient.getProcessInstanceByBusinessKey(businessKey));
            return engine;
        }
        return null;
    }

    public void startProcessInstance(){
        //do we want to start the process
    }

}
