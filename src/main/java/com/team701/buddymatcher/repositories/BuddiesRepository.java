package com.team701.buddymatcher.repositories;

import com.team701.buddymatcher.domain.users.Buddies;
import com.team701.buddymatcher.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuddiesRepository extends JpaRepository<Buddies, Long> {

    Long countByUser0OrUser1(User user0, User user1);
}
