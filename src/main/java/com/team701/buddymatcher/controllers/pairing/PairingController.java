package com.team701.buddymatcher.controllers.pairing;

import com.team701.buddymatcher.dtos.pairing.AddBuddyDTO;
import com.team701.buddymatcher.dtos.pairing.RemoveBuddyDTO;
import com.team701.buddymatcher.services.pairing.PairingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api
@RequestMapping("/api/pairing/")
public class PairingController {

    private final PairingService pairingService;

    @Autowired
    public PairingController(PairingService pairingService) {
        this.pairingService = pairingService;
    }

    @ApiOperation("Post method for adding a new buddy record between two users")
    @PostMapping(path = "/addBuddy/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addBuddy(@RequestBody AddBuddyDTO buddyRequest) {
        pairingService.addBuddy(buddyRequest.getUserId(), buddyRequest.getBuddyId());
        //Temporary return message since the addBuddy method is not implemented and this is a blank endpoint
        String result = String.format("\"Success: %s, %s \"",buddyRequest.getUserId(),buddyRequest.getBuddyId());
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @ApiOperation("Delete method for removing a buddy record between two users")
    @DeleteMapping(path = "/removeBuddy/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity removeBuddy(@RequestBody RemoveBuddyDTO buddyRequest) {
        pairingService.removeBuddy(buddyRequest.getUserId(), buddyRequest.getBuddyId());
        //Temporary return message since the removeBuddy method is not implemented and this is a blank endpoint
        String result = String.format("\"Removed: %s, %s \"",buddyRequest.getUserId(),buddyRequest.getBuddyId());
        return new ResponseEntity(result, HttpStatus.OK);
    }
}