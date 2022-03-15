package com.team701.buddymatcher.services.domain;

import com.team701.buddymatcher.domain.timetable.Course;
import com.team701.buddymatcher.domain.users.Buddies;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.timetable.CourseRepository;
import com.team701.buddymatcher.repositories.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InMemoryDBIntegrationTest {


    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByNameReturnCourse() {
        Long courseId = new Random().nextLong();

        Course course = createExpectedCourse(courseId);
        courseRepository.save(course);

        Course found = courseRepository.findByName(course.getName());

        Assertions.assertEquals(found.getName(),course.getName());
    }

    @Test
    public void whenAddingUserToCourseCorrectlyDisplayedForBoth() {
        Long courseId = new Random().nextLong();
        Long userId = new Random().nextLong();

        Course course = createExpectedCourse(courseId);
        User user = createExpectedUser(userId);

        courseRepository.save(course);
        userRepository.save(user);

        Set<User> users = new HashSet<>();
        users.add(user);
        course.setUsers(users);

        courseRepository.save(course);

        Course fetchedCourse = courseRepository.findByName(course.getName());

        Assertions.assertEquals(fetchedCourse.getCourseId(), courseId);
        Assertions.assertEquals(fetchedCourse.getStudentCount(), 1);
        Assertions.assertTrue(fetchedCourse.getUsers().contains(user));


    }

    @AfterEach
    public void clear() {
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    User createExpectedUser(Long id) {
        return new User()
                .setId(id)
                .setName("John Test")
                .setEmail("john.test@example.com")
                .setBuddies(new Buddies())
                .setCourses(new HashSet<>());
    }

    Course createExpectedCourse(Long id) {
        Course course = new Course();
        course.setCourseId(id);
        course.setName("se701");
        course.setSemester("2022 Sem 1");
        course.setStudentCount(0);
        course.setUsers(new HashSet<>());
        course.setUpdatedTime(Timestamp.from(Instant.now()));
        return course;
    }
}
