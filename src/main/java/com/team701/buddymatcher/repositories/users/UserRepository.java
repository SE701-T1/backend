package com.team701.buddymatcher.repositories.users;

import com.team701.buddymatcher.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.pairingEnabled = :pairingEnabled where u.id = :id")
    int updatePairingEnabled(@Param(value = "id") Long id, @Param(value = "pairingEnabled") Boolean pairingEnabled);

    @Query(value = "select User as u from User where u.email = ?1", nativeQuery = true)
    User findUserByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "insert into User (user_name, user_email) VALUES (?1, ?2)", nativeQuery=true)
    User createUser(String name, String email);
}
