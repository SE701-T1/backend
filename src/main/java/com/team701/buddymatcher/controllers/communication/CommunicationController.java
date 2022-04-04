package com.team701.buddymatcher.controllers.communication;

import com.team701.buddymatcher.controllers.users.UserController;
import com.team701.buddymatcher.domain.communication.Message;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.dtos.communication.ChatDTO;
import com.team701.buddymatcher.dtos.communication.MessageDTO;
import com.team701.buddymatcher.services.communication.CommunicationService;
import com.team701.buddymatcher.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserController userController;

    @Autowired
    public CommunicationController(CommunicationService communicationService,
                                   UserService userService,
                                   ModelMapper modelMapper,
                                   UserController userController) {
        this.communicationService = communicationService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userController = userController;
    }

    @Operation(summary = "Get method to get all messages sent to a given user")
    @GetMapping(path = "/messages/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MessageDTO>> getMessages(@Parameter(hidden = true)
                                                        @SessionAttribute("UserId") Long userId,
                                                        @PathVariable("id") Long buddyId) {
        try {
            this.userController.isUserValid(userId);
            this.userController.isUserValid(buddyId);
            List<Message> messages = this.communicationService.getMessages(userId, buddyId);
            List<MessageDTO> messageDTOs = messages.stream()
                    .map(message -> this.modelMapper.map(message, MessageDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(messageDTOs, HttpStatus.OK);
        } catch(NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Operation(summary = "Get method to get all chatroom's associated with a given user")
    @GetMapping(path = "/chatlist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatDTO>> getChatList(@Parameter(hidden = true)
                                                     @SessionAttribute("UserId") Long userId) {
        try {
            this.userController.isUserValid(userId);
            List<User> buddies = this.userService.retrieveBuddiesByUserId(userId);
            List<ChatDTO> chatDTOs = new ArrayList<>();

            for (User buddy : buddies) {
                Message lastMessage;
                lastMessage = this.communicationService.getLastMessage(userId, buddy.getId());
                if (lastMessage == null) {
                    lastMessage = new Message().setContent(null).setTimestamp(null);
                }
                chatDTOs.add(new ChatDTO()
                        .setId(buddy.getId())
                        .setName(buddy.getName())
                        .setLastMessage(lastMessage.getContent())
                        .setTimestamp(lastMessage.getTimestamp()));
            }
            return new ResponseEntity<>(chatDTOs, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }


    @Operation(summary = "Delete method to delete a user's conversation")
    @DeleteMapping(path = "/messages/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUserMessages(@Parameter(hidden = true) 
                                                   @SessionAttribute("UserId") Long userId, 
                                                   @PathVariable("id") Long buddyId) {
        try {
            // Check if the targeted user is valid
            userService.retrieveById(userId);
            userService.retrieveById(buddyId);
            
            communicationService.deleteMessagesBetweenUsers(userId, buddyId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Operation(summary = "Get method to search all messages sent to a given user by keywords or phrases")
    @GetMapping(path = "/messages/search/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MessageDTO>> getMessagesByWords(@Parameter(hidden = true)
                                                               @SessionAttribute("UserId") Long userId,
                                                               @PathVariable("id") Long buddyId,
                                                               @RequestParam("keywords") String keywords) {
        try {
            this.userController.isUserValid(userId);
            this.userController.isUserValid(buddyId);
            List<Message> messages = this.communicationService.getMessages(userId, buddyId);
            List<MessageDTO> messageDTOs = messages.stream()
                    .filter(message -> message.getContent().toUpperCase().contains(keywords.toUpperCase()))
                    .map(message -> this.modelMapper.map(message, MessageDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(messageDTOs, HttpStatus.OK);
        } catch(NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
