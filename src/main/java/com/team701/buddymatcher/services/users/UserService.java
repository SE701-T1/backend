package com.team701.buddymatcher.services.users;

import com.team701.buddymatcher.domain.users.User;

import java.util.List;
import java.util.NoSuchElementException;

public interface UserService {
    User retrieveById(Long id);
    User updatePairingEnabled(Long id, Boolean pairingEnabled);
    User retrieveByEmail(String email);
    void addUser(String name, String email);

    List<User> retrieveBuddiesByUserId(Long userId);
    Long countBuddies(User user);

    void addBuddy(Long currentUserId, Long buddyUserId) throws
            NoSuchElementException;
    void deleteBuddy(Long currentUserId, Long buddyUserId) throws NoSuchElementException;
}
