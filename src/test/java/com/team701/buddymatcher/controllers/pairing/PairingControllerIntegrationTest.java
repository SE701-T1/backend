package com.team701.buddymatcher.controllers.pairing;

import com.team701.buddymatcher.domain.timetable.Course;
import com.team701.buddymatcher.dtos.pairing.AddBuddyDTO;
import com.team701.buddymatcher.dtos.pairing.MatchBuddyDTO;
import com.team701.buddymatcher.dtos.pairing.RemoveBuddyDTO;
import com.team701.buddymatcher.services.pairing.PairingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class PairingControllerIntegrationTest {

    @Mock
    private PairingService pairingService;

    @InjectMocks
    private PairingController pairingController;

    @Test
    void addValidNewBuddy() {
        Long userId = new Random().nextLong();
        Long buddyId = new Random().nextLong();

        AddBuddyDTO buddyRequest = createMockedAddBuddyDTO(userId, buddyId);

        ResponseEntity response = pairingController.addBuddy(buddyRequest);

        //Temp response
        String success = String.format("\"Success: %s, %s \"", userId, buddyId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getBody(), success);
    }

    AddBuddyDTO createMockedAddBuddyDTO(Long userId, Long buddyId) {
        var dto = new AddBuddyDTO();
        dto.setUserId(userId);
        dto.setBuddyId(buddyId);
        return dto;
    }

    @Test 
    void removeValidBuddy() {
        Long userId = new Random().nextLong();
        Long buddyId = new Random().nextLong();

        RemoveBuddyDTO buddyRequest = createMockedRemoveBuddyDTO(userId, buddyId);

        ResponseEntity response = pairingController.removeBuddy(buddyRequest);

        //Temp response
        String success = String.format("\"Removed: %s, %s \"", userId, buddyId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getBody(), success);
    }

    @Test
    void requestBuddyMatchFromCourseWithPossibleBuddies() {

    }

    @Test
    void requestBuddyMatchFromCourseWithoutBuddies() {
        Random random = new Random();
        Long userId = random.nextLong();
        List<Long> courseIds = new ArrayList<>() {
            {
                add(random.nextLong());
                add(random.nextLong());
                add(random.nextLong());
            }
        };
        List<Course> courses = courseIds.stream()
                .map(id -> createExpectedCourse(id))
                .collect(Collectors.toList());


        MatchBuddyDTO matchRequest = new MatchBuddyDTO();
        matchRequest.setCourseIds(courseIds);

        ResponseEntity response = pairingController.matchBuddy(userId,matchRequest);

        //Temp response
        String success = String.format("\"Match: %s, %s \"", userId, courseIds);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getBody(), success);
    }

    RemoveBuddyDTO createMockedRemoveBuddyDTO(Long userId, Long buddyId) {
        var dto = new RemoveBuddyDTO();
        dto.setUserId(userId);
        dto.setBuddyId(buddyId);
        return dto;
    }

    Course createExpectedCourse(Long id) {
        Course course = new Course();
        course.setCourseId(id);
        course.setName("se701");
        course.setSemester("2022 Sem 1");
        course.setUpdatedTime(Timestamp.from(Instant.now()));
        return course;
    }
}
