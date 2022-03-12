package com.team701.buddymatcher.repositories.users;

import com.team701.buddymatcher.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
