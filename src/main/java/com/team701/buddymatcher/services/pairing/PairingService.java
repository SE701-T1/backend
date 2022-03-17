package com.team701.buddymatcher.services.pairing;

import java.util.List;

/**
 *  Handles the incoming requests related to pairing of buddies endpoints
 */
public interface PairingService {
    void addBuddy(Long userId, Long buddyId);
    
    void removeBuddy(Long userId, Long buddyId);

    void matchBuddy(Long userId, List<Long> courseIds);
}
