package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.wrapper.MessageSender;
import org.symphonyoss.client.exceptions.MessagesException;
import org.symphonyoss.client.services.ChatListener;
import org.symphonyoss.symphony.clients.model.SymMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleMessageSender implements MessageSender {
    private final Scanner scanner = new Scanner(System.in);

    private final List<ChatListener> chatListenerList = new ArrayList<>();

    @Override
    public void sendMessage(SymMessage message) throws MessagesException {

    }

    @Override
    public void sendMessage(String message, boolean waitForResponse) {
        System.out.println(message);

        if(waitForResponse) {
            String response = scanner.next();
            for (ChatListener chatListener : chatListenerList) {
                SymMessage sysMessage = new SymMessage();
                sysMessage.setMessageText(response);
                chatListener.onChatMessage(sysMessage);
            }
        }
    }

    @Override
    public void addListener(ChatListener chatListener) {
        chatListenerList.add(chatListener);
    }

    @Override
    public void removeListener(ChatListener chatListener) {
        chatListenerList.remove(chatListener);
    }

    @Override
    public void removeLastMessage() {

    }
}
