package com.team701.buddymatcher.controllers.pairing;

import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.dtos.pairing.MatchBuddyDTO;
import com.team701.buddymatcher.dtos.users.UserDTO;
import com.team701.buddymatcher.services.pairing.PairingService;
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
@Tag(name="Pairing")
@RequestMapping("/api/pairing/")
@SessionAttributes("UserId")
@SecurityRequirement(name = "JWT")
public class PairingController {

    private final PairingService pairingService;
    private final ModelMapper modelMapper;

    @Autowired
    public PairingController(PairingService pairingService, ModelMapper modelMapper) {
        this.pairingService = pairingService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get method for finding possible matches for a user given a list of courses to match through")
    @GetMapping(path = "/matchBuddy",
                consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity matchBuddy(@Parameter(hidden = true) @SessionAttribute("UserId") Long userId, @RequestBody MatchBuddyDTO buddyMatchRequest) {
        try {
            List<User> results = pairingService.matchBuddy(userId,
                    buddyMatchRequest.getCourseIds());
            List<Long> currentBuddies = pairingService.getBuddyIds(userId);

            /**Filter the list of possible buddies,
             * 1. If the user is open to pairing,
             * 2. If they are the current user,
             * 3. If they are already a buddy of the user,
             * Finally map it to a UserDto*/

            List<UserDTO> matches = results.stream()
                    .filter(User::getPairingEnabled)
                    .filter(user -> !user.getId().equals(userId))
                    .filter(user -> !currentBuddies.contains(user.getId()))
                    .map(user -> modelMapper.map(user, UserDTO.class))
                    .collect(Collectors.toList());
            if (matches.size() == 0) {
                return new ResponseEntity(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity(matches, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
