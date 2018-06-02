package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.wrapper.MessageSender;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;
import org.symphonyoss.client.exceptions.StreamsException;
import org.symphonyoss.client.exceptions.UsersClientException;

import java.time.LocalDateTime;
import java.util.Scanner;

public class ClientMeetingPreparationProcessorTest {

    @org.junit.Test
    public void prep() throws InitException, AuthenticationException {
        SymphonyClientBuilder symphonyClientBuilder = new SymphonyClientBuilder() {
            @Override
            public MessageSender getNewSymphonyChat(SymphonyClient symphonyClient, String rmEmail) throws UsersClientException, StreamsException {
                return new ConsoleMessageSender();
            }
        };
        ClientMeetingController processor = new ClientMeetingController(symphonyClientBuilder);
        processor.notifyClientMeeting(new ClientMeetingEvent(
                "abc@email.com", "clientA", LocalDateTime.now().plusDays(1)
        ));
    }

    public static void main(String[] args) {
        new ClientMeetingPreparationProcessorTest().commandLineProcessor();
    }

    private void commandLineProcessor() {
        // create a scanner so we can read the command-line input
        Scanner scanner = new Scanner(System.in);

        //  prompt for the user's name
        System.out.print("Enter your name: ");

        // get their input as a String
        String username = scanner.next();

        // prompt for their age
        System.out.print("Enter your age: ");

        // get the age as an int
        int age = scanner.nextInt();

        System.out.println(String.format("%s, your age is %d", username, age));


    }
}