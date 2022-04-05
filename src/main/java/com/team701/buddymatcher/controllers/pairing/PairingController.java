package com.team701.buddymatcher.controllers.pairing;

import com.team701.buddymatcher.controllers.users.UserController;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.dtos.pairing.MatchBuddyDTO;
import com.team701.buddymatcher.dtos.users.UserDTO;
import com.team701.buddymatcher.services.pairing.PairingService;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserController userController;

    @Autowired
    public PairingController(PairingService pairingService,
                             UserService userService,
                             ModelMapper modelMapper,
                             UserController userController) {
        this.pairingService = pairingService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userController = userController;
    }

    @Operation(summary = "Post method for finding possible matches for a user given a list of courses to match through")
    @PostMapping(path = "/matchBuddy",
                consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> matchBuddy(@Parameter(hidden = true)
                                                    @SessionAttribute("UserId") Long userId,
                                                    @RequestBody MatchBuddyDTO buddyMatchRequest) {
        try {
            this.userController.isUserValid(userId);
            List<User> results = this.pairingService.matchBuddy(userId,
                    buddyMatchRequest.getCourseIds());
            List<Long> currentBuddies = this.pairingService.getBuddyIds(userId);

            /*
             * Filter the list of possible buddies,
             * 1. If the user is open to pairing,
             * 2. If they are the current user,
             * 3. If they are already a buddy of the user,
             * Finally map it to a UserDto
             */
            List<UserDTO> matches = results.stream()
                    .filter(User::getPairingEnabled)
                    .filter(user -> !currentBuddies.contains(user.getId()))
                    .map(user -> {
                        //Map the user object to a DTO and then set the buddy count
                        UserDTO dto = this.modelMapper.map(user, UserDTO.class);
                        dto.setBuddyCount(this.userService.countBuddies(user));
                        return dto;
                    })
                    .collect(Collectors.toList());
            if (matches.size() == 0) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "No content");
            }
            return new ResponseEntity<>(matches, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
