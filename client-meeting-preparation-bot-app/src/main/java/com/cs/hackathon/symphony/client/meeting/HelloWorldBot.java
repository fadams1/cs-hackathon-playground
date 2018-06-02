/*
 *
 * Copyright 2016 The Symphony Software Foundation
 *
 * Licensed to The Symphony Software Foundation (SSF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.cs.hackathon.symphony.client.meeting;

import nlp.NLPConfig;
import nlp.NLPConfigLoader;
import nlp.NLPService;
import nlp.model.Action;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.SymphonyClientConfig;
import org.symphonyoss.client.SymphonyClientConfigID;
import org.symphonyoss.client.exceptions.MessagesException;
import org.symphonyoss.client.exceptions.SymException;
import org.symphonyoss.client.model.Chat;
import org.symphonyoss.client.services.ChatListener;
import org.symphonyoss.client.services.ChatServiceListener;
import org.symphonyoss.symphony.clients.model.SymMessage;
import org.symphonyoss.symphony.clients.model.SymUser;

import java.net.URL;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class HelloWorldBot implements ChatListener, ChatServiceListener{
    private final static Logger log = LoggerFactory.getLogger(HelloWorldBot.class);

    private SymphonyClientConfig config;
    private SymphonyClient symClient;
    private Chat chat;

    public static void main(String[] args) throws Exception {
        new HelloWorldBot();
        Utils.hang();
    }

    public HelloWorldBot() throws SymException {
        // Get SJC instance
        String configPath = Paths.get("src/main/resources/symphony.properties").toFile().getAbsolutePath();
        config = new SymphonyClientConfig(configPath);
        this.symClient = Utils.getSymphonyClient(config);

        // Init chat
//        this.chat = new Chat();
//        chat.setLocalUser(symClient.getLocalUser());
        this.symClient.getChatService().addListener(this);

//        // Add users to chat
        Set<SymUser> remoteUsers = new HashSet<>();
        remoteUsers.add(symClient.getUsersClient().getUserFromEmail(config.get(SymphonyClientConfigID.RECEIVER_EMAIL)));
        chat.setRemoteUsers(remoteUsers);
        chat.setStream(symClient.getStreamsClient().getStream(remoteUsers));
//
//        // Send a message
//        String message = "Hello " + config.get(SymphonyClientConfigID.RECEIVER_EMAIL) + "!";
//        Utils.sendMessage(symClient, chat, message);

    }

    @Override
    public void onChatMessage(SymMessage message) {
        log.info("message received");
        URL nlpUrl = getClass().getResource("nlp-config.json");
        NLPConfig nlpConfig = NLPConfigLoader.loadFromFile(nlpUrl.getPath());
        NLPService nlp = new NLPService(nlpConfig);
        List<Action> actions = nlp.match(message.getMessageText());
        log.info(actions.toString());

        try {
            Chat chat = this.symClient.getChatService().getChatByStream(message.getStreamId());
//            Utils.sendMessage(this.symClient, chat, message.getMessageText());
            Utils.sendMessage(this.symClient, chat, StringUtils.join(actions.stream().map(Action::getAction).collect(toList()), ","));
        } catch (MessagesException e) {
            e.printStackTrace();
            log.error("Error sending message", e);
        }
    }

    @Override
    public void onNewChat(Chat chat) {
        log.debug("on new chat invoked; registering listener, so messages get parsed");
        chat.addListener(this);

    }

    @Override
    public void onRemovedChat(Chat chat) {
        log.debug("on removed chat invoked; removing EchoBot as chat listener");
        chat.removeListener(this);

    }
}
