package com.cs.hackathon.symphony;


import camunda.TaskClient;
import camunda.model.ProcessInstance;
import camunda.model.Task;
import org.camunda.bpm.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowEngine.class);

    private String engineName;
    private ProcessInstance instance;


    public WorkflowEngine(String name, ProcessInstance instance) {
        this.engineName = name;
        this.instance = instance;
    }

    public String getName() {
        return engineName;
    }

    public ProcessInstance getInstance() {
        return instance;
    }

    public void completeTask(String userEmail){
        TaskClient client = new TaskClientBuilder().getNewTaskClient(true);
        Task task = client.getTask(getInstance().getId(), userEmail);
        client.completeTask(task.getId());
        LOGGER.info("Completed task " + task.getId() + " for user " + userEmail);
    }

}
