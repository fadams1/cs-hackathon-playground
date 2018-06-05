package com.cs.hackathon.symphony.client.meeting.topics.legalid;

import com.cs.hackathon.symphony.ThrowingFunction;
import com.cs.hackathon.symphony.client.meeting.ClientMeetingEvent;
import com.cs.hackathon.symphony.client.meeting.init.RmConversationInitiator;
import com.cs.hackathon.symphony.client.meeting.topics.TopicHandler;
import com.cs.hackathon.symphony.client.meeting.topics.TopicInformation;
import com.cs.hackathon.symphony.wrapper.MessageSender;
import nlp.model.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.symphonyoss.client.exceptions.MessagesException;
import org.symphonyoss.client.services.ChatListener;
import org.symphonyoss.symphony.clients.model.SymMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.cs.hackathon.symphony.client.meeting.init.RmConversationInitiator.CLIENT_IDS_ARE_IN_ORDER;

public class LegalIdTopicHandler implements TopicHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LegalIdTopicHandler.class);
    private final LegalIdRepository legalIdRepository;

    public LegalIdTopicHandler(LegalIdRepository legalIdRepository) {
        this.legalIdRepository = legalIdRepository;
    }

    @Override
    public TopicInformation collectTopicInformation(ClientMeetingEvent clientMeetingEvent, Action action, MessageSender rmChat) throws MessagesException {
        Set<LegalIdInformation> legalIdInformationFor = legalIdRepository.getLegalIdInformationFor(clientMeetingEvent.getClientId());
        if (!areAnyLegalIdsExpiredOrNearingExpiry(legalIdInformationFor)) {
            rmChat.sendMessage(CLIENT_IDS_ARE_IN_ORDER, false);
        }
        List<LegalIdInformation> legalIdsToDiscuss = legalIdInformationFor.stream()
                .map(checkLegalId(rmChat)).filter(Optional::isPresent).map(Optional::get)
                .collect(Collectors.toList());
        return new LegalIdTopicInformation(
                legalIdsToDiscuss
        );
    }

    private boolean areAnyLegalIdsExpiredOrNearingExpiry(Set<LegalIdInformation> legalIdInformationFor) {
        return legalIdInformationFor.stream().anyMatch(isExpired().or(isNearingExpiry()));
    }

    private ThrowingFunction<LegalIdInformation, Optional<LegalIdInformation>> checkLegalId(MessageSender rmChat) {
        return legalIdInformation -> {
            if (isExpired().test(legalIdInformation)) {
                return askAboutLegalId(rmChat, legalIdInformation, RmConversationInitiator.LEGAL_ID_EXPIRED);
            } else if(isNearingExpiry().test(legalIdInformation)) {
                return askAboutLegalId(rmChat, legalIdInformation, RmConversationInitiator.LEGAL_ID_NEARING_EXPIRY);
            } else {
                return Optional.empty();
            }
        };
    }

    private Optional<LegalIdInformation> askAboutLegalId(MessageSender rmChat, LegalIdInformation legalIdInformation, Function<LegalIdInformation, String> messageProducer) throws MessagesException, InterruptedException {
        CountDownLatch waitForResponse = new CountDownLatch(1);
        AtomicBoolean include = new AtomicBoolean(true);
        ChatListener chatListener = new ChatListener() {
            @Override
            public void onChatMessage(SymMessage symMessage) {
                LOGGER.info("Client replied with: {}", symMessage.getMessageText());
                include.set(symMessage.getMessageText().toLowerCase().contains("no"));
                waitForResponse.countDown();
            }
        };
        rmChat.removeLastMessage();
        rmChat.addListener(chatListener);
        rmChat.sendMessage(messageProducer.apply(legalIdInformation), false);
        rmChat.sendMessage(legalIdRepository.generateLegalIdCard(legalIdInformation));
        rmChat.sendMessage(RmConversationInitiator.CONFIRMATION, true);
        waitForResponse.await();
        rmChat.removeListener(chatListener);
        return include.get() ? Optional.of(legalIdInformation) : Optional.empty();
    }

    @Override
    public boolean isTopicRelevent() {
        return false;
    }

    private Predicate<LegalIdInformation> isExpired() {
        return legalIdInformation -> LocalDateTime.now().isAfter(legalIdInformation.getExpiry());
    }

    private Predicate<LegalIdInformation> isNearingExpiry() {
        return legalIdInformation -> LocalDateTime.now().plusMonths(3).isAfter(legalIdInformation.getExpiry());
    }
}