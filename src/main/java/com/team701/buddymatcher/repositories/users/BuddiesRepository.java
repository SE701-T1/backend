package com.team701.buddymatcher.repositories.users;

import com.team701.buddymatcher.domain.users.Buddies;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BuddiesRepository extends JpaRepository<Buddies, Long>{

}