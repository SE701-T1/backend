package com.team701.buddymatcher.services.pairing;

/**
 *  Handles the incoming requests related to pairing of buddies endpoints
 */
public interface PairingService {
    void addBuddy(String userId, String buddyId);
    
    void removeBuddy(String userId, String buddyId);
}
