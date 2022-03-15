package com.team701.buddymatcher.controllers.users;

import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.dtos.users.UserDTO;
import com.team701.buddymatcher.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@RestController
@Tag(name = "Users")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get method to retrieve a user by id")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> retrieveUserById(@PathVariable("id") Long id) {
        try {
            User user = userService.retrieve(id);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Operation(summary = "Put method to update a user's pairingEnabled field")
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updatePairingEnabled(@PathVariable("id") Long id, @RequestParam Boolean pairingEnabled) {
        try {
            User user = userService.updatePairingEnabled(id, pairingEnabled);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

}
