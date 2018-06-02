package com.cs.hackathon.symphony.wrapper;

import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.exceptions.MessagesException;
import org.symphonyoss.client.model.Chat;
import org.symphonyoss.client.services.ChatListener;
import org.symphonyoss.symphony.clients.model.SymMessage;

public class SymphonyMessageSender implements MessageSender {
    private final SymphonyClient client;
    private final Chat chat;

    public SymphonyMessageSender(SymphonyClient client, Chat chat) {
        this.client = client;
        this.chat = chat;
    }

    @Override
    public void sendMessage(String message, boolean waitForResponse) throws MessagesException {
        SymMessage messageSubmission = new SymMessage();
        messageSubmission.setMessageText(message);

        client.getMessageService().sendMessage(chat, messageSubmission);
    }

    @Override
    public void addListener(ChatListener chatListener) {
        chat.addListener(chatListener);
    }

    @Override
    public void removeListener(ChatListener chatListener) {
        chat.removeListener(chatListener);
    }
}
