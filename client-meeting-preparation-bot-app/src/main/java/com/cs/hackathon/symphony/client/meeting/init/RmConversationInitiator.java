package com.cs.hackathon.symphony.client.meeting.init;

import com.cs.hackathon.symphony.client.meeting.ClientMeetingEvent;
import com.cs.hackathon.symphony.client.meeting.topics.legalid.LegalIdInformation;

import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class RmConversationInitiator {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static final String CONFIRMATION =
            "Do you want to inlcude this in the client meeting preparation pack?";

    public static final Function<ClientMeetingEvent, String> HELLO_RM = request ->
            "Hello. You have a meeting with " + request.getClientId() + " at " + request.getClientMeetingTime().format(DATE_TIME_FORMATTER);

    public static final String WHAT_DO_YOU_WANT_TO_DISCUSS_WITH_THE_CLIENT =
            "Do you know what you want to discuss with the client?";

    // LEGAL ID
    public static final Function<LegalIdInformation, String> LEGAL_ID_EXPIRED = legalIdInformation ->
            "The clients " + legalIdInformation.getLegalIdType() + " expired on " + legalIdInformation.getExpiry().format(DATE_TIME_FORMATTER);

    public static final Function<LegalIdInformation, String> LEGAL_ID_NEARING_EXPIRY = legalIdInformation ->
            "The clients " + legalIdInformation.getLegalIdType() + " will expire on " + legalIdInformation.getExpiry().format(DATE_TIME_FORMATTER) + ". " +
                    "Would you like to add the clients " + legalIdInformation.getLegalIdType() + " to the client meeting preparation pack?";


    public static final String CLIENT_IDS_ARE_IN_ORDER = "All of the clients legal ids are valid for at least the next 3 months";
}
