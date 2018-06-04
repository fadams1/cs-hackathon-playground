package com.cs.hackathon.symphony;

import camunda.CamundaClient;
import camunda.CamundaClientException;
import camunda.ProcessInstanceClient;
import camunda.model.ProcessInstance;
import camunda.model.ProcessInstanceList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ProcessInstanceClientProxy extends ProcessInstanceClient {
    private String camundaEngineURL;
    private final Logger logger = LoggerFactory.getLogger(ProcessInstanceClientProxy.class);

    public ProcessInstanceClientProxy(String camundaURL) {
        super(camundaURL);
        this.camundaEngineURL = camundaURL;
    }

    public ProcessInstance getProcessInstanceByBusinessKey(String businessKey){
        Client client = ClientBuilder.newClient();
        Response response
                = client.target(camundaEngineURL)
                .path("process-instance/")
                .queryParam("businessKey", businessKey)
                .request(MediaType.APPLICATION_JSON)
                .get();
        if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
            try {
                handleError(response);
            } catch (CamundaClientException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            ProcessInstanceList instanceList =  response.readEntity(ProcessInstanceList.class);
            System.out.println(instanceList.get(0).getId());
            return instanceList.get(0);
        }
    }

    void handleError(Response response) throws CamundaClientException {
        try {
            if (response.getStatus() == 400) {
                this.logger.error("Client error occurred");
                throw new CamundaClientException(response.getStatusInfo().getReasonPhrase());
            }

            if (response.getStatus() == 401) {
                this.logger.error("User unauthorized");
                throw new CamundaClientException(response.getStatusInfo().getReasonPhrase());
            }

            if (response.getStatus() == 403) {
                this.logger.error("Forbidden");
                throw new CamundaClientException(response.getStatusInfo().getReasonPhrase());
            }

            if (response.getStatus() == 500) {
                this.logger.error("Internal server error");
                throw new CamundaClientException(response.getStatusInfo().getReasonPhrase());
            }
        } catch (Exception var3) {
            this.logger.error("Unexpected error");
            var3.printStackTrace();
        }

    }

}
