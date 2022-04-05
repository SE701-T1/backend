package com.team701.buddymatcher.services.communication.impl;

import com.team701.buddymatcher.domain.communication.Message;
import com.team701.buddymatcher.services.users.impl.UserServiceImpl;
import com.team701.buddymatcher.repositories.communication.MessageRepository;
import com.team701.buddymatcher.services.communication.CommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Implementation for the CommunicationService which is a service providing the implementations of the methods
 * for the communication endpoints
 */
@Service
public class CommunicationServiceImpl implements CommunicationService {

    private final MessageRepository messageRepository;
    private final UserServiceImpl userService;

    @Autowired
    public CommunicationServiceImpl(MessageRepository messageRepository, UserServiceImpl userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @Override
    public List<Message> getMessages(Long userId, Long buddyId) {
        return this.messageRepository.findMessagesBetweenUsers(userId, buddyId);
    }

    @Override
    public void readMessages(Long userId, Long buddyId) {
        this.messageRepository.updateUnreadMessagesForAUser(userId, buddyId);
    }

    @Override
    public void sendMessage(Message message) {
        this.messageRepository.save(message);
    }

    @Override
    public Message getLastMessage(Long userId, Long buddyId) {
        return this.messageRepository.findLastMessageBetweenUsers(userId, buddyId);
    }

    @Override
    public void addMessage(Long userId, Long buddyId, String message) {
        this.messageRepository.createMessage(userId, buddyId, message);
    }

    @Override
    public void deleteMessagesBetweenUsers(Long userId, Long buddyId) {
        messageRepository.deleteMessagesBetweenUsers(userId, buddyId);
    }
}
