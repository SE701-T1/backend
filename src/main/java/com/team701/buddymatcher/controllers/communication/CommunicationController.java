package com.team701.buddymatcher.controllers.communication;

import com.team701.buddymatcher.dtos.communication.MessageDTO;
import com.team701.buddymatcher.services.communication.CommunicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "")
@RequestMapping("/api/communication")
public class CommunicationController {

    private final CommunicationService communicationService;

    @Autowired
    public CommunicationController(CommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @Operation(summary = "Get method to get all messages sent to a given user")
    @GetMapping(path = "/messages/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable("userId") String userId) {
        List<MessageDTO> messages = communicationService.getMessages(userId);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @Operation(summary = "Post method to send a message to another user")
    @PostMapping(path = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> sendMessage(MessageDTO message) {
        communicationService.sendMessage(message);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
