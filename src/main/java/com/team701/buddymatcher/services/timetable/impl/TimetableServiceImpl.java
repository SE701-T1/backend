package com.team701.buddymatcher.services.timetable.impl;

import com.team701.buddymatcher.domain.timetable.Course;
import com.team701.buddymatcher.domain.timetable.Timetable;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.timetable.CourseRepository;
import com.team701.buddymatcher.services.timetable.TimetableService;
import com.team701.buddymatcher.services.users.UserService;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class TimetableServiceImpl implements TimetableService {

    private final CourseRepository courseRepository;
    private final UserService userService;

    @Autowired
    public TimetableServiceImpl(CourseRepository courseRepository, UserService userService) {
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
     * This method parses the input .ics file and retrieves the course names from the file.
     *
     * @param file file to upload
     * @return A list of string that contains the courses from the ics file.
     */
    @Override
    public List<String> getCalInfoFromIcs(File file) throws Exception {
        Calendar calendar;
        FileInputStream fin = new FileInputStream(file);
        CalendarBuilder builder = new CalendarBuilder();
        calendar = builder.build(fin);
        List<String> result = new ArrayList<>();

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
        //Remove the duplicate course names from the calendarInfo list
        List<String> courseList = new ArrayList<>();
        for (String course : result) {
            if (!courseList.contains(course)) {
                courseList.add(course);
            }
        }

        return courseList;
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

    public List<User> getUsersFromCourseIds(List<Long> courseIds) { return courseRepository.findAllUsersByCourseIds(courseIds);}
}
