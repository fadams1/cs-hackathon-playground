package com.cs.hackathon.symphony.client.followup;

import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.ThrowingFunction;
import com.cs.hackathon.symphony.client.meeting.ClientMeetingEvent;
import com.cs.hackathon.symphony.wrapper.MessageSender;
import nlp.model.Action;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.exceptions.AuthenticationException;
import org.symphonyoss.client.exceptions.InitException;
import org.symphonyoss.client.services.ChatListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

public class FollowupProcessor {
    private final SymphonyClientBuilder symphonyClientBuilder;
    private final SymphonyClient symphonyClient;
    private final Function<ClientMeetingFollowupEvent, CallReportTask> getCallReport;


    public FollowupProcessor(SymphonyClientBuilder symphonyClientBuilder) throws InitException, AuthenticationException {
        this.symphonyClientBuilder = symphonyClientBuilder;
        this.symphonyClient = symphonyClientBuilder.getNewSymphonyClient();

        getCallReport = notifyRmOfPendingTask()
                .andThen(startCallReport());
    }

    public CallReportTask getCallReportTask(ClientMeetingFollowupEvent event) {
        return getCallReport.apply(event);
    }

    private ThrowingFunction<FollowupRequestContainer, CallReportTask> startCallReport() {
        return followupRequest -> {
          if(followupRequest.getAction().getAction().equals("Yes")) {
              return new CallReportTask();
          }
          else  {
              MessageSender chat = followupRequest.getRmChat();
              chat.sendMessage("Okay thank you, bye", false);
              return new CallReportTask();
          }
        };
    }

    private ThrowingFunction<ClientMeetingFollowupEvent, FollowupRequestContainer> notifyRmOfPendingTask() {
        return clientMeetingFollowupEvent -> {
            ClientMeetingEvent pastMeetingEvent = clientMeetingFollowupEvent.getClientMeetingEvent();
            MessageSender initialChat = symphonyClientBuilder.getNewSymphonyChat(symphonyClient, pastMeetingEvent.getRmEmail());
            CountDownLatch messageWaiter = new CountDownLatch(1);
            final List<Action> rmResponse = new ArrayList<>();

            ChatListener chatListener = message -> {
                System.out.println("RM responded with: " + message);
                Action response = new Action();
                response.setAction("Yes");
                rmResponse.add(response);
                messageWaiter.countDown();
            };

            initialChat.addListener(chatListener);
            initialChat.sendMessage("Hello Rm, since you just completed a meeting with " +
                    pastMeetingEvent.getClientId() +
                    ", would you like to initiate a Call Report?",
                    false);

            messageWaiter.await();
            initialChat.removeListener(chatListener);

            return new FollowupRequestContainer(initialChat, symphonyClient, rmResponse.get(0), clientMeetingFollowupEvent);
        };
    }
}
