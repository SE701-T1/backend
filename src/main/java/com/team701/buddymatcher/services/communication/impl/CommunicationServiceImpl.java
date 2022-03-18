package com.team701.buddymatcher.services.communication.impl;

import com.team701.buddymatcher.domain.communication.Message;
import com.team701.buddymatcher.repositories.communication.MessageRepository;
import com.team701.buddymatcher.services.communication.CommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationServiceImpl implements CommunicationService {

    private final MessageRepository messageRepository;

    @Autowired
    public CommunicationServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> getMessages(Long userId, Long buddyId) {
        return messageRepository.findMessagesBetweenUsers(userId, buddyId);
    }

    @Override
    public void readMessages(Long userId, Long buddyId) {
        messageRepository.updateUnreadMessagesForAUser(userId, buddyId);
    }

    @Override
    public void sendMessage(Message message) {
        messageRepository.save(message);
    }
}
