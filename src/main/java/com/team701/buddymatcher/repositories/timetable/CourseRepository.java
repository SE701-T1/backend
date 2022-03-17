package com.team701.buddymatcher.repositories.timetable;

import com.team701.buddymatcher.domain.timetable.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByName(String name);

    @Query("SELECT c FROM Course c JOIN c.users u WHERE u.id=:userId")
    List<Course> findByUserId(@Param("userId") Long userId);
}
