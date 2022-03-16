package com.team701.buddymatcher.controllers.users;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.Json;
import com.google.api.client.json.webtoken.JsonWebToken;
import com.team701.buddymatcher.config.JwtTokenUtil;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.dtos.users.UserDTO;
import com.team701.buddymatcher.services.users.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.NoSuchElementException;

@RestController
@Api
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @ApiOperation("Get method to retrieve a user by id")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> retrieveUserById(@PathVariable("id") Long id) {
        try {
            User user = userService.retrieveById(id);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @ApiOperation(value = "Put method to update a user's pairingEnabled field")
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

    @ApiOperation(value = "Post method for a user logging in using Google")
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loginWithGoogle(HttpServletRequest request) throws GeneralSecurityException, IOException {
        String CLIENT_ID = "react";
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), new GsonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
        String idTokenString = request.getHeader("id_token");

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken != null) {
            Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");

            // Use or store profile information
            // ...
            User user = userService.retrieveByEmail(email);
            if (user == null){ // assuming if not found it'll return null
                // Persist new user to the database
                userService.addUser(name, email);
            }
                // return custom JWT
            JwtTokenUtil util = new JwtTokenUtil();
            String token = util.generateToken(user);
            return new ResponseEntity<>(token, HttpStatus.OK);


        } else {
            System.out.println("Invalid ID token.");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }



}
