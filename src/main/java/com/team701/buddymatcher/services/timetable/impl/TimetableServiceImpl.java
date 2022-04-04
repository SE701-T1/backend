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
import net.fortuna.ical4j.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.NoSuchElementException;

@Service
public class TimetableServiceImpl implements TimetableService {

    private final BuddiesRepository buddiesRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;

    @Autowired
    public TimetableServiceImpl(CourseRepository courseRepository,
                                BuddiesRepository buddiesRepository,
                                UserService userService) {
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
     * @param input An input stream containing ics data
     * @return A list of string that contains the courses from the ics file.
     */
    @Override
    public List<String> getCalInfoFromIcs(InputStream input) throws ParserException, IOException {
        Calendar calendar;
        CalendarBuilder builder = new CalendarBuilder();
        calendar = builder.build(input);

        Set<String> result = new HashSet<>();
        for (net.fortuna.ical4j.model.component.CalendarComponent calendarComponent : calendar.getComponents()) {
            for (Property value : calendarComponent.getProperties()) {
                String event;
                if ("SUMMARY".equals(value.getName())) {
                    event = value.getValue();
                    result.add(event);
                }
            }
        }
        return new ArrayList<>(result);
    }

    @Transactional
    public void populateCourses(Long studentId, List<String> courseNames) {
        User user = this.userService.retrieveById(studentId);
        for (String courseName : courseNames) {
            Course course = this.courseRepository.findByName(courseName);

            // if the course does not exist in the database, create a new record
            if (course == null) {
                Course newCourse = createNewCourse(courseName);
                newCourse.addNewUser(user);
                this.courseRepository.save(newCourse);
            } else {
                course.addNewUser(user);
                this.courseRepository.save(course);
            }
        }
    }

    private Course createNewCourse(String courseName) {
        Course newCourse = new Course();
        newCourse.setName(courseName);
        newCourse.setUpdatedTime(Timestamp.from(Instant.now()));
        //TODO: setup a method of determining semester time, for now its hardcoded
        newCourse.setSemester("Semester 1 2022");
        return newCourse;
    }

    @Override
    public List<Course> getCourses(Long userId) {
        return this.courseRepository.findByUserId(userId);
    }

    @Override
    public Course getCourse(Long courseId) throws NoSuchElementException {
        return this.courseRepository.findById(courseId).orElseThrow();
    }

    @Override
    public Long getBuddyCountFromCourse(Long userId, Long courseId) {
        Long countUser0 = this.buddiesRepository.countUser0(userId, courseId);
        Long countUser1 = this.buddiesRepository.countUser1(userId, courseId);
        return countUser0 + countUser1;
    }

    public List<User> getUsersFromCourseIds(List<Long> courseIds) throws NoSuchElementException {
        for (Long id : courseIds)
            this.getCourse(id); // check if course ID exists in database
        return this.courseRepository.findAllUsersByCourseIds(courseIds);
    }

    /**
     * Remove a course a user is enrolled in matching a given course ID and user ID
     * @param userId the ID of the user the course is removed from
     * @param courseId the ID of the course being removed from the user
     */
    @Override
    @Transactional
    public void deleteCourse(Long userId, Long courseId) {
        User user = this.userService.retrieveById(userId);
        List<Course> courses = this.getCourses(userId);
        for (Course course : courses) {
            if (course.getCourseId() == courseId) {
                course.removeUser(user);
                this.courseRepository.save(course);
                break;
            }
        }
    }
}
