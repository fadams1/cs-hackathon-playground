package com.cs.hackathon.symphony.client.meeting.topics.legalid;

import org.symphonyoss.symphony.clients.model.SymMessage;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LegalIdMap implements LegalIdRepository {
    private final Map<String, Set<LegalIdInformation>> legalIdMap = new HashMap<>();

    public LegalIdMap() {
        legalIdMap.put("fay", faysLegalIds());
        legalIdMap.put("mj", mjsLegalIds());
    }

    private Set<LegalIdInformation> faysLegalIds() {
        Set<LegalIdInformation> faysIds = new HashSet<>();
        faysIds.add(new LegalIdInformation("Expired Passport", LocalDateTime.now().minusDays(1)));
        faysIds.add(new LegalIdInformation("Almost expired Passport", LocalDateTime.now().plusMonths(2)));
        faysIds.add(new LegalIdInformation("Passport", LocalDateTime.now().plusMonths(5)));
        return faysIds;
    }

    private Set<LegalIdInformation> mjsLegalIds() {
        Set<LegalIdInformation> mjsLegalIds = new HashSet<>();
        mjsLegalIds.add(new LegalIdInformation("Passport1", LocalDateTime.now().plusMonths(5)));
        mjsLegalIds.add(new LegalIdInformation("Passport2", LocalDateTime.now().plusMonths(4)));
        return mjsLegalIds;
    }

    @Override
    public Set<LegalIdInformation> getLegalIdInformationFor(String clientId) {
        return legalIdMap.get(clientId);
    }

    @Override
    public SymMessage generateLegalIdCard(LegalIdInformation legalIdInformation) {
        SymMessage msg = new SymMessage();
        msg.setMessage("<div data-format=\"PresentationML\" data-version=\"2.0\"> " +
                "<div class=\"card\">\n" +
                "<div class=\"cardHeader\"><h1>Here are the legal ID information of your client</h1></div>\n" +
                "<div class=\"cardBody\">" +
                "<span>Legal ID Type: </span>\n" + legalIdInformation.getLegalIdType() +
                "<br/>\n" +
                "<hr/>\n"+
                "<span>Expiry Date: </span>\n" + legalIdInformation.getExpiry() +
                "</div>" +
                "</div>" +
                "</div>");

        return msg;
    }
}
