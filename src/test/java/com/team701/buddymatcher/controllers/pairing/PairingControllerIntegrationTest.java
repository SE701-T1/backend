package com.team701.buddymatcher.controllers.pairing;

import com.team701.buddymatcher.dtos.pairing.AddBuddyDTO;
import com.team701.buddymatcher.services.pairing.PairingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PairingControllerIntegrationTest {

    @Mock
    private PairingService pairingService;

    @InjectMocks
    private PairingController pairingController;

    @Test
    void addValidNewBuddy() {
        String userId = UUID.randomUUID().toString();
        String buddyId = UUID.randomUUID().toString();

        AddBuddyDTO buddyRequest = createMockedAddBuddyDTO(userId, buddyId);

        ResponseEntity response = pairingController.addBuddy(buddyRequest);

        //Temp response
        String success = String.format("\"Success: %s, %s \"", userId, buddyId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(response.getBody(), success);
    }

    AddBuddyDTO createMockedAddBuddyDTO(String userId, String buddyId) {
        var dto = new AddBuddyDTO();
        dto.setUserId(userId);
        dto.setBuddyId(buddyId);
        return dto;
    }
}
