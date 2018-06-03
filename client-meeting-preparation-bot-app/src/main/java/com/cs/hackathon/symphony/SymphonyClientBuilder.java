package com.cs.hackathon.symphony;

import com.cs.hackathon.symphony.wrapper.MessageSender;
import com.cs.hackathon.symphony.wrapper.SymphonyMessageSender;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.SymphonyClientConfig;
import org.symphonyoss.client.SymphonyClientFactory;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;
import org.symphonyoss.client.exceptions.StreamsException;
import org.symphonyoss.client.exceptions.UsersClientException;
import org.symphonyoss.client.model.Chat;
import org.symphonyoss.client.services.ChatListener;
import org.symphonyoss.symphony.clients.model.SymUser;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class SymphonyClientBuilder {

    public SymphonyClient getNewSymphonyClient() throws InitException, AuthenticationException {
        String configPath = Paths.get("src/main/resources/symphony.properties").toFile().getAbsolutePath();
        SymphonyClientConfig config = new SymphonyClientConfig(configPath);

        SymphonyClient symClient = SymphonyClientFactory.getClient(SymphonyClientFactory.TYPE.BASIC);

        symClient.init(config);
        return symClient;
    }

    public MessageSender getNewSymphonyChat(SymphonyClient symphonyClient, String rmEmail) throws UsersClientException, StreamsException {
        Chat initialChat = new Chat();
        initialChat.setLocalUser(symphonyClient.getLocalUser());

        Set<SymUser> remoteUsers = new HashSet<>();
        remoteUsers.add(symphonyClient.getUsersClient().getUserFromEmail(rmEmail));
        initialChat.setRemoteUsers(remoteUsers);
        initialChat.setStream(symphonyClient.getStreamsClient().getStream(remoteUsers));
        symphonyClient.getChatService().addChat(initialChat);
        return new SymphonyMessageSender(symphonyClient, initialChat);
    }

    public void subscribeToEvents(MessageSender messageSender, ChatListener chatListener) {
        messageSender.addListener(chatListener);
    }

    public void unsubscribeToEvents(MessageSender messageSender, ChatListener chatListener) {
        messageSender.removeListener(chatListener);
    }

}
