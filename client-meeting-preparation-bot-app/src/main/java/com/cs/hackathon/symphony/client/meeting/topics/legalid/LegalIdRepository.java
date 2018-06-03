package com.cs.hackathon.symphony.client.meeting.topics.legalid;

import org.symphonyoss.symphony.clients.model.SymMessage;

import java.util.Set;

public interface LegalIdRepository {
    Set<LegalIdInformation> getLegalIdInformationFor(String clientId);

    SymMessage generateLegalIdCard(LegalIdInformation legalIdInformation);
}
