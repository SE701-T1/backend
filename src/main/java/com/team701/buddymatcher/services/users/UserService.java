package com.team701.buddymatcher.services.users;

import com.team701.buddymatcher.domain.users.User;

public interface UserService {
    User retrieve(Long id);
    User updatePairingEnabled(Long id, Boolean pairingEnabled);
}
