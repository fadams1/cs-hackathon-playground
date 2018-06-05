package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.wrapper.MessageSender;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;
import org.symphonyoss.client.exceptions.StreamsException;
import org.symphonyoss.client.exceptions.UsersClientException;

import java.time.LocalDateTime;

public class ClientMeetingPreparationProcessorConsoleLocalRunner {

    public static void main(String[] args) throws InitException, AuthenticationException {
        SymphonyClientBuilder symphonyClientBuilder = new SymphonyClientBuilder() {
            @Override
            public MessageSender getNewSymphonyChat(SymphonyClient symphonyClient, String rmEmail) throws UsersClientException, StreamsException {
                return new ConsoleMessageSender();
            }

            @Override
            public SymphonyClient getNewSymphonyClient() throws InitException, AuthenticationException {
                return null;
            }
        };
        ClientMeetingController processor = new ClientMeetingController(symphonyClientBuilder, null);
        processor.notifyClientMeeting(new ClientMeetingEvent(
                "abc@email.com", "clientA", LocalDateTime.now().plusDays(1)
        ));
    }
}