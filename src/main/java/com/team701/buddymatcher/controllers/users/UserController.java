package com.team701.buddymatcher.controllers.users;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.team701.buddymatcher.config.JwtTokenUtil;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.dtos.users.UserDTO;
import com.team701.buddymatcher.services.users.UserService;
import com.team701.buddymatcher.services.timetable.TimetableService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Users")
@RequestMapping("/api/users")
@SessionAttributes("UserId")
@SecurityRequirement(name = "JWT")
public class UserController {

    // Set Google console client ID in environment variables: see frontend wiki "Authentication flow" for how
    public static final String CLIENT_ID = System.getenv("CLIENT_ID");

    private final UserService userService;
    private final TimetableService timetableService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, TimetableService timetableService, ModelMapper modelMapper) {
        this.userService = userService;
        this.timetableService = timetableService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Get method to retrieve self")
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> retrieveSelf(@Parameter(hidden = true) @SessionAttribute("UserId") Long userId) {
        return getUserById(userId);
    }

    @Operation(summary = "Get method to retrieve a user by id")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> retrieveUserById(@PathVariable("id") Long id) {
        return getUserById(id);
    }

    private ResponseEntity<UserDTO> getUserById(Long userId) {
        try {
            User user = userService.retrieveById(userId);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);

            Long buddyCount = userService.countBuddies(user);
            userDTO.setBuddyCount(buddyCount);

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Operation(summary = "Put method to update a own pairingEnabled field")
    @PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updatePairingEnabled(@Parameter(hidden = true) @SessionAttribute("UserId") Long userId, @RequestParam Boolean pairingEnabled) {
        try {
            User user = userService.updatePairingEnabled(userId, pairingEnabled);
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Operation(summary = "Get method for a user logging in using Google")
    @GetMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> loginWithGoogle(HttpServletRequest request) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), new GsonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();
        String idTokenString = request.getHeader("id_token");
        if (idTokenString == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        Payload payload = idToken.getPayload();

        // Print user identifier
        String userId = payload.getSubject();

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
            user = userService.retrieveByEmail(email);
        }
            // return custom JWT
        JwtTokenUtil util = new JwtTokenUtil();
        String token = util.generateToken(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @Operation(summary = "Get method to retrieve a list of buddy from a user")
    @GetMapping(path = "/buddy", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUserBuddy(@Parameter(hidden = true) @SessionAttribute("UserId") Long userId) {
        try {
            List<User> users = userService.retrieveBuddiesByUserId(userId);
            List<UserDTO> userDTOs = users.stream()
                    .map(user -> {
                        // setting buddy count for each of the buddy dto
                        UserDTO dto = modelMapper.map(user, UserDTO.class);
                        dto.setBuddyCount(userService.countBuddies(user));
                        return dto;
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Operation(summary = "Get method to retrieve a list of buddies of a user from a course by courseID")
    @GetMapping(path = "/buddy/course/{course_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUserBuddiesInCourse(@Parameter(hidden = true) @SessionAttribute("UserId") Long userId, @PathVariable("course_id") Long courseId) {
        // Get list of all users in the course
        List<User> usersInCourse;
        try {
            List<Long> courseList = new ArrayList<>();
            courseList.add(courseId);
            usersInCourse = timetableService.getUsersFromCourseIds(courseList);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        // Filter list of users to only those in the course given by courseID
        try {
            List<User> usersBuddiesAll = userService.retrieveBuddiesByUserId(userId);
            List<User> usersBuddiesCourse = new ArrayList<>();
            for (int i = 0; i < usersBuddiesAll.size(); i++) {
                for (int j = 0; j < usersInCourse.size(); j++) {
                    if (usersBuddiesAll.get(i).getId().equals(usersInCourse.get(j).getId())) {
                        User buddy = usersBuddiesAll.get(i);
                        usersBuddiesCourse.add(buddy);
                        continue;
                    }
                }
            }

            List<UserDTO> userDTOs = usersBuddiesCourse.stream()
                    .map(user -> {
                        // setting buddy count for each of the buddy dto
                        UserDTO dto = modelMapper.map(user, UserDTO.class);
                        dto.setBuddyCount(userService.countBuddies(user));
                        return dto;
                    })
                    .collect(Collectors.toList());

            return new ResponseEntity<>(userDTOs, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Operation(summary = "Post method insert a user as a buddy")
    @PostMapping(path = "/buddy/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> postUserBuddy(@Parameter(hidden = true) @SessionAttribute("UserId") Long userId, @PathVariable("id") Long buddyId) {
        try {
            userService.addBuddy(userId, buddyId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @Operation(summary = "Delete method to delete a user's buddy")
    @DeleteMapping(path = "/buddy/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteUserBuddy(@Parameter(hidden = true) @SessionAttribute("UserId") Long userId, @PathVariable("id") Long buddyId) {
        try {
            userService.deleteBuddy(userId, buddyId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    /**
     * TODO: remove fake login (for frontend testing)
     */
    @Operation(summary = "Get method for a fake Login for testing")
    @GetMapping(path = "/fake/login/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFakeLogin(@PathVariable("id") Long userId) {
        try {
            User user = userService.retrieveById(userId);
            JwtTokenUtil util = new JwtTokenUtil();
            String token = util.generateToken(user);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
        }
    }

    /**
     * TODO: remove fake register (for frontend testing)
     */
    @Operation(summary = "Get method for a fake Register for testing")
    @GetMapping(path = "/fake/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getFakeRegister(@RequestParam("name") String name, @RequestParam("email") String email) {
        try {
            userService.addUser(name, email);
            User user = userService.retrieveByEmail(email);
            UserDTO userDTO = new UserDTO()
                    .setId(user.getId())
                    .setEmail(user.getEmail())
                    .setName(user.getName())
                    .setPairingEnabled(user.getPairingEnabled());
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad Request");
        }
    }

    @Operation(summary = "Post method for blocking a user")
    @PostMapping(path = "/buddy/{id}/block", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> blockBuddy(@Parameter(hidden = true)
                                              @SessionAttribute("UserId") Long userBlockingId,
                                              @PathVariable("id") Long userBlockedId) {
        try {
            userService.blockBuddy(userBlockingId, userBlockedId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
