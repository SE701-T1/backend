package com.team701.buddymatcher.services.communication;

import com.team701.buddymatcher.dtos.communication.MessageDTO;

import java.util.List;

/**
 * Handles the incoming requests related to the communication endpoint
 */
public interface CommunicationService {
    List<MessageDTO> getMessages(String userId);

    void sendMessage(MessageDTO message);
}
