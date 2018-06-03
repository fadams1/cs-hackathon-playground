package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.SymphonyClientBuilder;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;

import java.time.LocalDateTime;

public class ClientMeetingMain {

    public static void main(String[] args) throws InitException, AuthenticationException {
        new ClientMeetingController(new SymphonyClientBuilder()).notifyClientMeeting(
                new ClientMeetingEvent("marianne.celino@credit-suisse.com",
                        "fay", LocalDateTime.now().plusDays(1))
        );
    }
}