package com.team701.buddymatcher.repositories.communication;

import com.team701.buddymatcher.domain.communication.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    /**
     * Finds all the messages between 2 users in date order
     * Assuring integrity of the data user0 != user1 and user0 < user1
     * @param user0Id - first user
     * @param user1Id - 2nd user
     */
    @Query(value = "SELECT * FROM (SELECT * FROM Messages m WHERE m.sender_id=:user0 AND m.receiver_id=:user1 UNION SELECT * FROM Messages m WHERE m.sender_id=:user1 AND m.receiver_id=:user0) ORDER BY timestamp ASC", nativeQuery=true)
    List<Message> findMessagesBetweenUsers(@Param("user0") Long user0Id, @Param("user1") Long user1Id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Message m set m.isRead=true WHERE m.receiver.id=:userId AND m.sender.id=:buddyUserId AND m.isRead=false")
    void updateUnreadMessagesForAUser(@Param("userId") Long userId, @Param("buddyUserId") Long buddyUserId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO Messages (sender_id, receiver_id, timestamp, content) VALUES (:senderId, :receiverId, CURRENT_TIMESTAMP(), :message)", nativeQuery=true)
    void createMessage(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId, @Param("message") String message);

    /**
     * Finds the last message between 2 users
     * Assuring integrity of the data user0 != user1 and user0 < user1
     * @param user0Id - first user
     * @param user1Id - 2nd user
     */
    @Query(value = "SELECT * FROM (SELECT * FROM Messages m WHERE m.sender_id=:user0 AND m.receiver_id=:user1 UNION SELECT * FROM Messages m WHERE m.sender_id=:user1 AND m.receiver_id=:user0) ORDER BY timestamp DESC LIMIT 1", nativeQuery=true)
    Message findLastMessageBetweenUsers(@Param("user0") Long user0Id, @Param("user1") Long user1Id);
}
