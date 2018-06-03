package com.cs.hackathon.symphony.client.meeting.topics.legalid;

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
        faysIds.add(new LegalIdInformation("Expired Passport", LocalDateTime.now().minusDays(1), "https://something.com"));
        faysIds.add(new LegalIdInformation("Almost expired Passport", LocalDateTime.now().plusMonths(2), "https://something.com"));
        faysIds.add(new LegalIdInformation("Passport", LocalDateTime.now().plusMonths(5), "https://something.com"));
        return faysIds;
    }

    private Set<LegalIdInformation> mjsLegalIds() {
        Set<LegalIdInformation> mjsLegalIds = new HashSet<>();
        mjsLegalIds.add(new LegalIdInformation("Passport1", LocalDateTime.now().plusMonths(5), "https://something.com"));
        mjsLegalIds.add(new LegalIdInformation("Passport2", LocalDateTime.now().plusMonths(4), "https://something.com"));
        return mjsLegalIds;
    }

    @Override
    public Set<LegalIdInformation> getLegalIdInformationFor(String clientId) {
        return legalIdMap.get(clientId);
    }
}
