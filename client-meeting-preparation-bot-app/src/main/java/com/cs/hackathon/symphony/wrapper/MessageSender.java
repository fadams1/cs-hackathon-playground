package com.cs.hackathon.symphony.wrapper;

import org.symphonyoss.client.exceptions.MessagesException;
import org.symphonyoss.client.services.ChatListener;
import org.symphonyoss.symphony.clients.model.SymMessage;

public interface MessageSender {
    void sendMessage(SymMessage message) throws MessagesException;

    void sendMessage(String message, boolean waitForReponse) throws MessagesException;

    void addListener(ChatListener chatListener);

    void removeListener(ChatListener chatListener);

    void removeLastMessage();
}
