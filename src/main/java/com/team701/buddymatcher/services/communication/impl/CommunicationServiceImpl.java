package com.team701.buddymatcher.services.communication.impl;

import com.team701.buddymatcher.dtos.communication.MessageDTO;
import com.team701.buddymatcher.services.communication.CommunicationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommunicationServiceImpl implements CommunicationService {
    @Override
    public List<MessageDTO> getMessages(String userId) {
        return new ArrayList<>();
    }

    @Override
    public void sendMessage(MessageDTO message) {

    }
}
