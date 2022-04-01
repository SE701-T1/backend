package com.team701.buddymatcher.services.timetable;

import com.team701.buddymatcher.domain.timetable.Course;
import com.team701.buddymatcher.domain.timetable.Timetable;
import com.team701.buddymatcher.domain.users.User;
import net.fortuna.ical4j.data.ParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface TimetableService {
    Timetable retrieve(String userId);
    List<String> getCalInfoFromIcs(InputStream input) throws ParserException, IOException;
    void populateCourses(Long studentId, List<String> courseNames);

    List<Course> getCourses(Long userId);
    Course getCourse(Long courseId);

    List<User> getUsersFromCourseIds(List<Long> courseIds);

    Long getBuddyCountFromCourse(Long userId, Long courseId);

    /**
     * Remove a course a user is enrolled in matching a given course ID and user ID
     * @param userId the ID of the user the course is removed from
     * @param courseId the ID of the course being removed from the user
     */
    boolean deleteCourse(Long userId, Long courseId);
}
