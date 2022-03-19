package com.team701.buddymatcher.services.timetable.impl;

import com.team701.buddymatcher.domain.timetable.Course;
import com.team701.buddymatcher.domain.timetable.Timetable;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.timetable.CourseRepository;
import com.team701.buddymatcher.repositories.users.BuddiesRepository;
import com.team701.buddymatcher.services.timetable.TimetableService;
import com.team701.buddymatcher.services.users.UserService;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class TimetableServiceImpl implements TimetableService {

    private final BuddiesRepository buddiesRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;

    @Autowired
    public TimetableServiceImpl(CourseRepository courseRepository, BuddiesRepository buddiesRepository, UserService userService) {
        this.buddiesRepository = buddiesRepository;
        this.courseRepository = courseRepository;
        this.userService = userService;
    }

    @Override
    public Timetable retrieve(String userId) {
        Timetable t = new Timetable();
        t.setClassNames(Arrays.asList("SE701", "SE754", "SE751"));
        return t;
    }

    /**
     * This method parses the input .ics stream and retrieves the course names from the file.
     *
     * @param input An input stream containing ics data
     * @return A list of string that contains the courses from the ics file.
     */
    @Override
    public List<String> getCalInfoFromIcs(InputStream input) throws ParserException, IOException {
        Calendar calendar;
        CalendarBuilder builder = new CalendarBuilder();
        calendar = builder.build(input);

        Set<String> result = new HashSet<>();

        for (Iterator i = calendar.getComponents().iterator(); i.hasNext(); ) {
            Component component = (Component) i.next();

            for (Iterator j = component.getProperties().iterator(); j.hasNext(); ) {
                try {
                    String event;
                    Property property = (Property) j.next();
                    if ("SUMMARY".equals(property.getName())) {
                        event = property.getValue();
                        result.add(event);
                    }

                } catch (Exception e) {

                }
            }
        }

        return new ArrayList<>(result);
    }

    @Transactional
    public void populateCourses(Long studentId, List<String> courseNames) {
        User user = userService.retrieveById(studentId);
        for (String courseName : courseNames) {
            Course course = courseRepository.findByName(courseName);

            //if the course does not exist in the database, create a new record
            if (course == null) {
                Course newCourse = createNewCourse(courseName);
                newCourse.addNewUser(user);
                courseRepository.save(newCourse);
            } else {
                course.addNewUser(user);
                courseRepository.save(course);
            }
        }
    }

    private Course createNewCourse(String courseName) {
        Course newCourse = new Course();
        newCourse.setName(courseName);
        newCourse.setUpdatedTime(Timestamp.from(Instant.now()));
        //TODO: setup a method of determining semester time, for now its hardcoded
        newCourse.setSemester("2022 Semester 1");
        return newCourse;
    }

    @Override
    public List<Course> getCourses(Long userId) {
        return courseRepository.findByUserId(userId);
    }

    @Override
    public Course getCourse(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow();
    }

    @Override
    public Long getBuddyCountFromCourse(Long userId, Long courseId) {
        Long countUser0 = buddiesRepository.countUser0(userId, courseId);
        Long countUser1 = buddiesRepository.countUser1(userId, courseId);

        return countUser0 + countUser1;
    }

    public List<User> getUsersFromCourseIds(List<Long> courseIds) { return courseRepository.findAllUsersByCourseIds(courseIds);}
}
