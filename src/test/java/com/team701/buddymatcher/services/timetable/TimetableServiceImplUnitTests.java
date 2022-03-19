package com.team701.buddymatcher.services.timetable;

import com.team701.buddymatcher.domain.timetable.Course;
import com.team701.buddymatcher.domain.users.Buddies;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.timetable.CourseRepository;
import com.team701.buddymatcher.services.timetable.impl.TimetableServiceImpl;
import com.team701.buddymatcher.services.users.UserService;
import net.fortuna.ical4j.data.ParserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class TimetableServiceImplUnitTests {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TimetableServiceImpl timetableService;

    @Test
    void correctlyGenerateCoursesInDatabase() {
        Long userId = new Random().nextLong();
        User user = createExpectedUser(userId);

        List<String> courseNames = Arrays.asList("se701", "se750", "enggen303");

        Mockito.when(userService.retrieveById(userId)).thenReturn(user);
        for (String courseName : courseNames) {
            Mockito.when(courseRepository.findByName(courseName)).thenReturn(null);
        }

        timetableService.populateCourses(userId, courseNames);

        Mockito.verify(courseRepository, times(3)).save(Mockito.any(Course.class));
    }

    @Test
    void correctlyGenerateCoursesAndAddToExistingCourse() {
        Long userId = new Random().nextLong();
        User user = createExpectedUser(userId);

        Long courseId = new Random().nextLong();
        Course course = createExpectedCourse(courseId);

        List<String> courseNames = Arrays.asList("se750", "se701", "enggen303");
        Mockito.when(courseRepository.findByName("se750")).thenReturn(null);
        Mockito.when(courseRepository.findByName("se701")).thenReturn(course);
        Mockito.when(courseRepository.findByName("enggen303")).thenReturn(null);

        Mockito.when(userService.retrieveById(userId)).thenReturn(user);

        timetableService.populateCourses(userId, courseNames);

        Assertions.assertEquals(course.getStudentCount(), 1);
        Mockito.verify(courseRepository, times(3)).save(Mockito.any(Course.class));
    }

    @Test
    void correctlyAddToExistingCourseWithOtherStudents() {
        Long userId = new Random().nextLong();
        User user = createExpectedUser(userId);

        Long courseId = new Random().nextLong();
        Course course = createExpectedCourse(courseId);

        course.addNewUser(new User().setId(new Random().nextLong()).setName("Matt Moran"));

        List<String> courseNames = Arrays.asList("se701");
        Mockito.when(courseRepository.findByName("se701")).thenReturn(course);

        Mockito.when(userService.retrieveById(userId)).thenReturn(user);

        timetableService.populateCourses(userId, courseNames);

        Assertions.assertEquals(course.getStudentCount(), 2);
        Mockito.verify(courseRepository, times(1)).save(Mockito.any(Course.class));
    }

    @Test
    void getCourses() {
        Random random = new Random();
        Long id = random.nextLong();
        List<Course> expected = new ArrayList<>() {
            {
                add(createExpectedCourse(random.nextLong()));
                add(createExpectedCourse(random.nextLong()));
                add(createExpectedCourse(random.nextLong()));
            }
        };
        Mockito.when(courseRepository.findByUserId(id)).thenReturn(expected);

        List<Course> course = timetableService.getCourses(id);

        Assertions.assertNotNull(course);
        Assertions.assertEquals(course, expected);
    }

    @Test
    void getCourse() {
        Long id = new Random().nextLong();
        Course expected = createExpectedCourse(id);
        Mockito.when(courseRepository.findById(id)).thenReturn(Optional.of(expected));

        Course course = timetableService.getCourse(id);

        Assertions.assertNotNull(course);
        Assertions.assertEquals(course, expected);
    }

    @Test
    void correctlyExtractCourseNamesFromIcsFile() throws IOException, ParserException {
        List<String> expected = Arrays.asList(
                "SOFTENG 754",
                "SOFTENG 701",
                "COMPSYS 725",
                "ENGGEN 403",
                "SOFTENG 761 LAB",
                "SOFTENG 761",
                "SOFTENG 751"
        );
        File icsFile = new File("src/test/resources/UoACal.ics");
        InputStream icsInput = new FileInputStream(icsFile);
        List<String> courses = timetableService.getCalInfoFromIcs(icsInput);
        Assertions.assertEquals(courses, expected);
    }

    User createExpectedUser(Long id) {
        return new User()
                .setId(id)
                .setName("Pink Elephant")
                .setEmail("pink.elephant@gmail.com")
                .setPairingEnabled(false);
    }

    Course createExpectedCourse(Long id) {
        Course course = new Course();
        course.setCourseId(id);
        course.setName("se701");
        course.setSemester("2022 Sem 1");
        course.setUpdatedTime(Timestamp.from(Instant.now()));
        return course;
    }

}
