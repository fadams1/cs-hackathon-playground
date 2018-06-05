package com.cs.hackathon.symphony.client.meeting.topics.legalid;

import org.symphonyoss.symphony.clients.model.SymMessage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LegalIdMap implements LegalIdRepository {
    private static final String LEGAL_ID_ML = "" +
            "<messageML>" +
                    "<div class=\"card\">" +
                        "<div class=\"cardBody\">" +
                            "<h1>Here are the legal ID information of your client</h1>" +
                            "<br/>" +
                            "<span>Legal ID Type: ${entity['legalIdType']}</span>" +
                            "<br/>" +
                            "<hr/>" +
                            "<span style=\"color:red\">Expiry Date: ${entity['legalIdExpiry']}</span>" +
                        "</div>" +
                    "</div>" +
            "</messageML>";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final Map<String, Set<LegalIdInformation>> legalIdMap = new HashMap<>();

    public LegalIdMap() {
        legalIdMap.put("fay", faysLegalIds());
        legalIdMap.put("mj", mjsLegalIds());
        legalIdMap.put("mcelino", mjsLegalIds());
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

    private Set<LegalIdInformation> mcelinoLegalIds() {
        Set<LegalIdInformation> mjsLegalIds = new HashSet<>();
        mjsLegalIds.add(new LegalIdInformation("Passport1", LocalDateTime.now().plusMonths(1)));
        mjsLegalIds.add(new LegalIdInformation("Passport2", LocalDateTime.now().plusMonths(2)));
        return mjsLegalIds;
    }

    @Override
    public Set<LegalIdInformation> getLegalIdInformationFor(String clientId) {
        return legalIdMap.get(clientId);
    }

    @Override
    public SymMessage generateLegalIdCard(LegalIdInformation legalIdInformation) {
        SymMessage msg = new SymMessage();

        msg.setMessage(LEGAL_ID_ML);
        msg.setEntityData("{" +
                "    \"legalIdType\":" + "\""+ legalIdInformation.getLegalIdType() + "\"," +
                "    \"legalIdExpiry\":" + "\""+ legalIdInformation.getExpiry().format(DATE_TIME_FORMATTER) + "\"" +
                "}");

        return msg;
    }
}
