package com.team701.buddymatcher.services.users;

import com.team701.buddymatcher.domain.users.User;

public interface UserService {
    User retrieveById(Long id);
    User updatePairingEnabled(Long id, Boolean pairingEnabled);
    User retrieveByEmail(String email);
    void addUser(String name, String email);
}
