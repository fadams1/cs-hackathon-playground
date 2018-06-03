package com.cs.hackathon.symphony.client.meeting.topics.legalid;

import java.util.Set;

public interface LegalIdRepository {
    Set<LegalIdInformation> getLegalIdInformationFor(String clientId);

}
