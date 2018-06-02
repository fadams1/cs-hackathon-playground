package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.wrapper.ClientMessageSender;

public class ConsoleClientMessageSender implements ClientMessageSender {

    @Override
    public void sendMessage(String message) {
        System.out.println(message);
    }
}
