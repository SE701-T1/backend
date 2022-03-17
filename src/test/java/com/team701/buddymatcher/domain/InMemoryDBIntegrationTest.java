package com.team701.buddymatcher.domain;

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
import java.util.Optional;
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
        Course course = createExpectedCourse();
        courseRepository.save(course);

        Course found = courseRepository.findByName(course.getName());

        Assertions.assertEquals(found.getName(),course.getName());
    }

    @Test
    public void whenFindByNameDoesNotReturnCourse() {
        Course found = courseRepository.findByName("se702");
        Assertions.assertNull(found);
    }

    @Test
    public void createUserAndFindThem() {
        User user = createExpectedUser();
        Long userId = userRepository.save(user).getId();

        Optional<User> found = userRepository.findById(userId);

        Assertions.assertTrue(found.isPresent());
        User foundUser = found.get();
        Assertions.assertEquals(foundUser.getName(), user.getName());
    }

    @Test
    public void whenAddingUserToCourseCorrectlyDisplayedForBoth() {
        Course course = createExpectedCourse();
        User user = createExpectedUser();

        Long courseId = courseRepository.save(course).getCourseId();
        Long userId = userRepository.save(user).getId();

        course.addNewUser(user);
        courseRepository.save(course);

        Course fetchedCourse = courseRepository.findByName(course.getName());

        System.out.println(fetchedCourse.getStudentCount());

        Assertions.assertEquals(fetchedCourse.getCourseId(), courseId);
        Assertions.assertEquals(fetchedCourse.getStudentCount(), 1);
        Assertions.assertTrue(fetchedCourse.getUsers().contains(user));

        User fetchedUser = userRepository.getById(userId);

        Assertions.assertEquals(fetchedUser.getName(), user.getName());
        Assertions.assertTrue(fetchedUser.getCourses().size() == 1);


    }

    @AfterEach
    public void clear() {
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    User createExpectedUser() {
        return new User()
                .setName("John Test")
                .setEmail("john.test@example.com");
    }

    Course createExpectedCourse() {
        Course course = new Course();
        course.setName("se701");
        course.setSemester("2022 Sem 1");
        course.setUpdatedTime(Timestamp.from(Instant.now()));
        return course;
    }
}
