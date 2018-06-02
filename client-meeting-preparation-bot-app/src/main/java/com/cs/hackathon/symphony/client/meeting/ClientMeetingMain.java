package com.cs.hackathon.symphony.client.meeting;

import java.time.LocalDateTime;

public class ClientMeetingMain {

    public static void main(String[] args) {
        new ClientMeetingController().notifyClientMeeting(
                new ClientMeetingEvent("noopur.n.jain@credit-suisse.com",
                        "fay", LocalDateTime.now().plusDays(1))
        );
    }
}
