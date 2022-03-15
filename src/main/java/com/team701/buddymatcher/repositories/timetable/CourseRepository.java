package com.team701.buddymatcher.repositories.timetable;

import com.team701.buddymatcher.domain.timetable.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    public Course findByName(String name);
}
