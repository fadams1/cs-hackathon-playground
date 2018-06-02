package com.cs.hackathon.symphony;

import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.SymphonyClientConfig;
import org.symphonyoss.client.SymphonyClientFactory;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;

import java.nio.file.Paths;

public class SymphonyClientBuilder {

    public SymphonyClient getNewSymphonyClient() throws InitException, AuthenticationException {
        String configPath = Paths.get("src/main/resources/symphony.properties").toFile().getAbsolutePath();
        SymphonyClientConfig config = new SymphonyClientConfig(configPath);

        SymphonyClient symClient = SymphonyClientFactory.getClient(SymphonyClientFactory.TYPE.BASIC);

        symClient.init(config);
        return symClient;
    }
}
