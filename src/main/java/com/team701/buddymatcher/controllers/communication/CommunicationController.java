package com.team701.buddymatcher.controllers.communication;

import com.team701.buddymatcher.domain.communication.Message;
import com.team701.buddymatcher.dtos.communication.MessageDTO;
import com.team701.buddymatcher.services.communication.CommunicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Communication")
@RequestMapping("/api/communication")
@SessionAttributes("UserId")
@SecurityRequirement(name = "JWT")
public class CommunicationController {

    private final CommunicationService communicationService;

    private ModelMapper modelMapper;

    @Autowired
    public CommunicationController(CommunicationService communicationService, ModelMapper modelMapper) {
        this.communicationService = communicationService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get method to get all messages sent to a given user")
    @GetMapping(path = "/messages/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MessageDTO>> getMessages(@Parameter(hidden = true) @SessionAttribute("UserId") Long userId, @PathVariable("id") Long buddyId) {
        try {
            List<Message> messages = communicationService.getMessages(userId, buddyId);
            List<MessageDTO> messageDTOs = messages.stream()
                    .map(message -> modelMapper.map(message, MessageDTO.class))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(messageDTOs, HttpStatus.OK);
        } catch(NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
