package com.team701.buddymatcher.controllers.pairing;

import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.dtos.pairing.AddBuddyDTO;
import com.team701.buddymatcher.dtos.pairing.MatchBuddyDTO;
import com.team701.buddymatcher.dtos.pairing.RemoveBuddyDTO;
import com.team701.buddymatcher.dtos.users.UserDTO;
import com.team701.buddymatcher.services.pairing.PairingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name="Pairing")
@RequestMapping("/api/pairing/")
public class PairingController {

    private final PairingService pairingService;

    private final ModelMapper modelMapper;

    @Autowired
    public PairingController(PairingService pairingService, ModelMapper modelMapper) {
        this.pairingService = pairingService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Post method for adding a new buddy record between two users")
    @PostMapping(path = "/addBuddy/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addBuddy(@RequestBody AddBuddyDTO buddyRequest) {
        pairingService.addBuddy(buddyRequest.getUserId(), buddyRequest.getBuddyId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Delete method for removing a buddy record between two users")
    @DeleteMapping(path = "/removeBuddy/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeBuddy(@RequestBody RemoveBuddyDTO buddyRequest) {
        pairingService.removeBuddy(buddyRequest.getUserId(), buddyRequest.getBuddyId());
        return new ResponseEntity(HttpStatus.OK);
    }

    @Operation(summary = "Get method for finding possible matches for a user given a list of courses to match through")
    @GetMapping(path = "/matchBuddy/{userId}",
                consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity matchBuddy(@PathVariable("userId") Long userId, @RequestBody MatchBuddyDTO buddyMatchRequest) {
        List<User> results = pairingService.matchBuddy(userId,buddyMatchRequest.getCourseIds());
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
        return new ResponseEntity(matches,HttpStatus.OK);
    }
}