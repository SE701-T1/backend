package com.team701.buddymatcher.repositories.users;

import com.team701.buddymatcher.domain.users.Buddies;
import com.team701.buddymatcher.domain.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BuddiesRepository extends JpaRepository<Buddies, Long> {

    Long countByUser0OrUser1(User user0, User user1);

    @Query(value="SELECT COUNT(*) FROM Buddies b JOIN Course_Student cs ON b.user_1_id=cs.user_id WHERE b.user_0_id=:userId AND cs.course_id=:courseId", nativeQuery=true)
    Long countUser0(Long userId, Long courseId);

    @Query(value="SELECT COUNT(*) FROM Buddies b JOIN Course_Student cs ON b.user_0_id=cs.user_id WHERE b.user_1_id=:userId AND cs.course_id=:courseId", nativeQuery=true)
    Long countUser1(Long userId, Long courseId);
}
