package com.cs.hackathon.symphony.client.meeting;

import com.cs.hackathon.symphony.SymphonyClientBuilder;
import com.cs.hackathon.symphony.ThrowingConsumer;
import com.cs.hackathon.symphony.client.meeting.topics.legalid.LegalIdInformation;
import com.cs.hackathon.symphony.client.meeting.topics.legalid.LegalIdMap;
import com.cs.hackathon.symphony.client.meeting.topics.legalid.LegalIdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.SymphonyClient;
import org.symphonyoss.client.exceptions.*;
import org.symphonyoss.client.model.Chat;
import org.symphonyoss.client.services.ChatListener;
import org.symphonyoss.client.services.ChatServiceListener;
import org.symphonyoss.symphony.clients.model.SymMessage;

import java.util.Set;

public class LegalIdCheckingBotMain implements ChatListener, ChatServiceListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(LegalIdCheckingBotMain.class);
    private final LegalIdRepository legalIdRepository = new LegalIdMap();
    private final SymphonyClient client;
    private String clientId;

    public LegalIdCheckingBotMain(SymphonyClient client) {
        this.client = client;
    }

    public static void main(String[] args) throws InitException, AuthenticationException, UsersClientException, StreamsException {
        SymphonyClientBuilder symphonyClientBuilder = new SymphonyClientBuilder();
        SymphonyClient syClient = symphonyClientBuilder.getNewSymphonyClient();

        syClient.getChatService().addListener(new LegalIdCheckingBotMain(syClient));
    }

    @Override
    public void onNewChat(Chat chat) {
        chat.addListener(new ChatListener() {
            @Override
            public void onChatMessage(SymMessage symMessage) {
                String clientId = symMessage.getMessageText().trim();
                LOGGER.info("LegalId review requested for: {}", clientId);
                sendLegalIds(clientId, chat);
            }
        });
    }

    @Override
    public void onRemovedChat(Chat chat) {

    }

    @Override
    public void onChatMessage(SymMessage symMessage) {

    }

    private void sendLegalIds(String clientId, Chat chat) {
        Set<LegalIdInformation> legalIds = legalIdRepository.getLegalIdInformationFor(clientId);
        legalIds.forEach(getLegalIdInformationConsumer(chat));
        if (legalIds.isEmpty()) {
            SymMessage msg = new SymMessage();
            msg.setMessageText("No legal ids found for " + clientId);
            try {
                client.getMessageService().sendMessage(chat, msg);
            } catch (MessagesException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ThrowingConsumer<LegalIdInformation> getLegalIdInformationConsumer(Chat chat) {
        return legalId -> {
            SymMessage message = legalIdRepository.generateLegalIdCard(legalId);
            client.getMessageService().sendMessage(chat, message);
        };
    }
}
