package com.team701.buddymatcher.services.communication;

import com.team701.buddymatcher.domain.communication.Message;

import java.util.List;

/**
 * Handles the incoming requests related to the communication endpoint
 */
public interface CommunicationService {
    List<Message> getMessages(Long userId, Long buddyId);

    void readMessages(Long userId, Long buddyId);

    void sendMessage(Message message);

    Message getLastMessage(Long userId, Long buddyId);

    void addMessage(Long userId, Long buddyId, String message);
}
