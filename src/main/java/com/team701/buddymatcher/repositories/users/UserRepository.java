package com.team701.buddymatcher.repositories.users;

import com.team701.buddymatcher.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.pairingEnabled = :pairingEnabled where u.id = :id")
    int updatePairingEnabled(@Param(value = "id") Long id, @Param(value = "pairingEnabled") Boolean pairingEnabled);

    @Query(value = "select u from User u where u.email = ?1")
    User findUserByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "insert into User (user_name, user_email) VALUES (:name, :email)", nativeQuery=true)
    void createUser(@Param("name") String name, @Param("email") String email);

    @Query(value = "SELECT * FROM User u JOIN Buddies b ON b.user_0_id=u.id WHERE b.user_1_id=:userId UNION SELECT * FROM User u JOIN Buddies b ON b.user_1_id=u.id WHERE b.user_0_id=:userId", nativeQuery=true)
    List<User> findBuddies(@Param("userId") Long userId);

    /**
     * Create buddy creates a buddy pair
     * To assure integrity of the data user0 != user1 and user0 < user1
     * @param user0 - first user
     * @param user1 - 2nd user
     */
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO Buddies (user_0_id, user_1_id) VALUES (:user0, :user1)", nativeQuery=true)
    void createBuddy(@Param("user0") Long user0, @Param("user1") Long user1);

    /**
     * Delete buddy deletes a buddy pair
     * To assure integrity of the data user0 != user1 and user0 < user1
     * @param user0 - first user
     * @param user1 - 2nd user
     */
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM Buddies b WHERE b.user0.id=:user0 AND b.user1.id=:user1")
    void deleteBuddy(@Param("user0") Long user0, @Param("user1") Long user1);
}
