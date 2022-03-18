package com.team701.buddymatcher.services.timetable;

import com.team701.buddymatcher.domain.timetable.Course;
import com.team701.buddymatcher.domain.timetable.Timetable;
import com.team701.buddymatcher.domain.users.User;

import java.io.File;
import java.util.List;

public interface TimetableService {
    Timetable retrieve(String userId);
    List<String> getCalInfoFromIcs(File file) throws Exception;
    void populateCourses(Long studentId, List<String> courseNames);

    List<Course> getCourses(Long userId);
    Course getCourse(Long courseId);

    List<User> getUsersFromCourseIds(List<Long> courseIds);
}
