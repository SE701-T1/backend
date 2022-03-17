package com.team701.buddymatcher.repositories.users;

import com.team701.buddymatcher.domain.users.Buddies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BuddiesRepository extends JpaRepository<Buddies, Long>{

    public Optional<Buddies> findById(Long id);
}