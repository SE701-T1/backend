package com.team701.buddymatcher.domain;

import com.team701.buddymatcher.domain.communication.Message;
import com.team701.buddymatcher.domain.timetable.Course;
import com.team701.buddymatcher.domain.timetable.Timetable;
import com.team701.buddymatcher.domain.users.BlockedBuddies;
import com.team701.buddymatcher.domain.users.Buddies;
import com.team701.buddymatcher.domain.users.ReportedBuddies;
import com.team701.buddymatcher.domain.users.User;
import com.team701.buddymatcher.repositories.communication.MessageRepository;
import com.team701.buddymatcher.repositories.timetable.CourseRepository;
import com.team701.buddymatcher.repositories.users.BlockedBuddiesRepository;
import com.team701.buddymatcher.repositories.users.BuddiesRepository;
import com.team701.buddymatcher.repositories.users.ReportedBuddiesRepository;
import com.team701.buddymatcher.repositories.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "socketio.host=localhost",
        "socketio.port=8086"
})
public class InMemoryDBIntegrationTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuddiesRepository buddiesRepository;

    @Autowired
    private BlockedBuddiesRepository blockedBuddiesRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ReportedBuddiesRepository reportedBuddiesRepository;

    @Test
    public void whenFindByNameReturnCourse() {
        Course course = createExpectedCourse();
        Timetable timeTable = new Timetable();
        timeTable.setClassNames(List.of(course.getName()));
        courseRepository.save(course);
        Course found = courseRepository.findByName(course.getName());
        Assertions.assertEquals(found.getName(), course.getName());
        Assertions.assertEquals(found.getCourseId(), course.getCourseId());
        Assertions.assertEquals(found.getStudentCount(), course.getStudentCount());
        Assertions.assertEquals(found.getSemester(), course.getSemester());
        Assertions.assertEquals(found.getUpdatedTime(), course.getUpdatedTime());
        Assertions.assertEquals(found.getUsers(), course.getUsers());
        Assertions.assertEquals(timeTable.getClassNames(), List.of(course.getName()));

        found = found.setName("se350");
        course = course.setName(found.getName());
        Assertions.assertEquals(found.getName(), course.getName());

        found = found.setCourseId(2);
        course = course.setCourseId(found.getCourseId());
        Assertions.assertEquals(found.getCourseId(), course.getCourseId());

        found = found.setSemester("2099 Semester Zero");
        course = course.setSemester(found.getSemester());
        Assertions.assertEquals(found.getSemester(), course.getSemester());

        found = found.setStudentCount(5);
        course = course.setStudentCount(found.getStudentCount());
        Assertions.assertEquals(found.getStudentCount(), course.getStudentCount());

        User newUser = createExpectedUser();
        Set<User> users = new HashSet<>();
        users.add(newUser);
        found = found.setUsers(users);
        course = course.setUsers(found.getUsers());
        Assertions.assertEquals(found.getUsers(), course.getUsers());

        found = found.setUpdatedTime(Timestamp.valueOf("1111-11-11 11:11:11"));
        course = course.setUpdatedTime(found.getUpdatedTime());
        Assertions.assertEquals(found.getUpdatedTime(), course.getUpdatedTime());

        found = found.addNewUser(newUser);
        course = course.removeUser(newUser);
        found = found.removeUser(newUser);
        Assertions.assertEquals(found.getUsers(), course.getUsers());
    }

    @Test
    public void whenFindByNameDoesNotReturnCourse() {
        Course found = courseRepository.findByName("se702");
        Assertions.assertNull(found);
    }

    @Test
    public void whenFindByUserIdReturnCourses() {
        Course createdCourse = createExpectedCourse();
        User createdUser = createExpectedUser();
        courseRepository.save(createdCourse);
        createdCourse.addNewUser(createdUser);
        Set<User> users = createdCourse.getUsers();
        for (User user : users) {
            List<Course> courses = courseRepository.findByUserId(user.getId());
            for (Course course : courses) {
                Assertions.assertEquals(course.getName(), createdCourse.getName());
                Assertions.assertEquals(course.getCourseId(), createdCourse.getCourseId());
                Assertions.assertEquals(course.getStudentCount(), createdCourse.getStudentCount());
                Assertions.assertEquals(course.getSemester(), createdCourse.getSemester());
                Assertions.assertEquals(course.getUpdatedTime(), createdCourse.getUpdatedTime());
                Assertions.assertEquals(course.getUsers(), createdCourse.getUsers());
            }
        }
    }

    @Test
    public void whenFindByCourseIdsReturnUsers() {
        Course createdCourse = createExpectedCourse();
        User createdUser = createExpectedUser();
        courseRepository.save(createdCourse);
        createdCourse.addNewUser(createdUser);
        List<Long> courseIds = new ArrayList<>();
        courseIds.add(createdCourse.getCourseId());
        List<User> users = courseRepository.findAllUsersByCourseIds(courseIds);
        for (User user : users) {
            Assertions.assertEquals(user.getName(), createdUser.getName());
            Assertions.assertEquals(user.getId(), createdUser.getId());
            Assertions.assertEquals(user.getEmail(), createdUser.getEmail());
            Assertions.assertEquals(user.getPairingEnabled(), createdUser.getPairingEnabled());
            Assertions.assertEquals(user.getCourses(), createdUser.getCourses());

            user = user.setName("naaaaaame");
            createdUser = createdUser.setName(user.getName());
            Assertions.assertEquals(user.getName(), createdUser.getName());

            user = user.setId(3L);
            createdUser = createdUser.setId(user.getId());
            Assertions.assertEquals(user.getId(), createdUser.getId());

            user = user.setEmail("wrong@email.com");
            createdUser = createdUser.setEmail(user.getEmail());
            Assertions.assertEquals(user.getEmail(), createdUser.getEmail());

            user = user.setPairingEnabled(true);
            createdUser = createdUser.setPairingEnabled(user.getPairingEnabled());
            Assertions.assertEquals(user.getPairingEnabled(), createdUser.getPairingEnabled());

            Set<Course> courses = new HashSet<>();
            courses.add(createdCourse);
            user = user.setCourses(courses);
            createdUser = createdUser.setCourses(user.getCourses());
            Assertions.assertEquals(user.getCourses(), createdUser.getCourses());
        }
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
        Assertions.assertEquals(fetchedCourse.getCourseId(), courseId);
        Assertions.assertEquals(fetchedCourse.getStudentCount(), 1);
        Assertions.assertTrue(fetchedCourse.getUsers().contains(user));

        User fetchedUser = userRepository.getById(userId);
        Assertions.assertEquals(fetchedUser.getName(), user.getName());
        Assertions.assertEquals(1, fetchedUser.getCourses().size());
    }

    @Test
    public void addRemoveBuddies() {
        Course course = createExpectedCourse();
        Long courseId = courseRepository.save(course).getCourseId();
        User userOne = createExpectedUser();
        User userTwo = createExpectedBuddy();
        userOne = userOne.setPairingEnabled(true);
        userTwo = userTwo.setPairingEnabled(true);
        Long userOneId = userRepository.save(userOne).getId();
        Long userTwoId = userRepository.save(userTwo).getId();
        course.addNewUser(userOne);
        course.addNewUser(userTwo);
        courseRepository.save(course);

        userRepository.createBuddy(userOneId, userTwoId);
        Buddies buddies = buddiesRepository.getById(1L);
        Long buddiesId = buddiesRepository.save(buddies).getId();
        Assertions.assertEquals(buddiesId, buddies.getId());
        Assertions.assertEquals(buddies.getUser0().getId(), userOne.getId());
        Assertions.assertEquals(buddies.getUser1().getId(), userTwo.getId());
        Long count = buddiesRepository.countByUser0OrUser1(userOne, userTwo);
        Assertions.assertEquals(count, 1L);
        count = buddiesRepository.countUser0(userOneId, courseId);
        Assertions.assertEquals(count, 1L);
        count = buddiesRepository.countUser1(userTwoId, courseId);
        Assertions.assertEquals(count, 1L);

        buddies = buddies.setUser0(userTwo);
        buddies = buddies.setUser1(userOne);
        buddiesRepository.save(buddies);
        Assertions.assertEquals(buddies.getUser0().getId(), userTwo.getId());
        Assertions.assertEquals(buddies.getUser1().getId(), userOne.getId());

        userRepository.deleteBuddy(userOneId, userTwoId);
        buddiesRepository.save(buddies);
        count = buddiesRepository.countByUser0OrUser1(userOne, userTwo);
        Assertions.assertEquals(count, 0L);

        Long newId = buddies.getId() + 1;
        buddies = buddies.setId(newId);
        buddiesRepository.save(buddies);
        Assertions.assertEquals(buddies.getId(), newId);
        buddies = buddies.setId(newId - 1);
        buddiesRepository.save(buddies);
    }

    @Test
    public void blockBuddies() {
        Course course = createExpectedCourse();
        User userBlocker = createExpectedUser();
        User userBlocked = createExpectedBuddy();
        Long userBlockerId = userRepository.save(userBlocker).getId();
        Long userBlockedId = userRepository.save(userBlocked).getId();
        course.addNewUser(userBlocker);
        course.addNewUser(userBlocked);
        courseRepository.save(course);

        blockedBuddiesRepository.addBlockedBuddy(userBlockerId, userBlockedId);
        BlockedBuddies blockedBuddies = blockedBuddiesRepository.getById(1L);
        Long blockedBuddiesId = blockedBuddiesRepository.save(blockedBuddies).getId();
        blockedBuddiesRepository.save(blockedBuddies);
        Assertions.assertEquals(blockedBuddiesId, blockedBuddies.getId());
        Assertions.assertEquals(blockedBuddies.getUserBlocker().getId(), userBlocker.getId());
        Assertions.assertEquals(blockedBuddies.getUserBlocked().getId(), userBlocked.getId());

        blockedBuddies = blockedBuddies.setUserBlocker(userBlocked);
        blockedBuddies = blockedBuddies.setUserBlocked(userBlocker);
        blockedBuddiesRepository.save(blockedBuddies);
        Assertions.assertEquals(blockedBuddies.getUserBlocker().getId(), userBlocked.getId());
        Assertions.assertEquals(blockedBuddies.getUserBlocked().getId(), userBlocker.getId());
        List<User> blockedUsers = userRepository.getBlockedBuddies(userBlockedId);
        for (User user : blockedUsers) {
            Assertions.assertEquals(user.getId(), userBlocker.getId());
        }

        Long newId = blockedBuddies.getId() + 1;
        blockedBuddies = blockedBuddies.setId(newId);
        blockedBuddiesRepository.save(blockedBuddies);
        Assertions.assertEquals(blockedBuddies.getId(), newId);
        blockedBuddies = blockedBuddies.setId(newId - 1);
        blockedBuddiesRepository.save(blockedBuddies);
    }

    @Test
    public void messageUser() {
        User userSender = createExpectedUser();
        User userReceiver = createExpectedBuddy();
        userSender = userSender.setPairingEnabled(true);
        userReceiver = userReceiver.setPairingEnabled(true);
        Long userSenderId = userRepository.save(userSender).getId();
        Long userReceiverId = userRepository.save(userReceiver).getId();

        messageRepository.createMessage(userSenderId, userReceiverId, "Hello World!");
        Message message = messageRepository.getById(1L);
        Long messagesId = messageRepository.save(message).getId();
        Assertions.assertEquals(messagesId, message.getId());
        Assertions.assertEquals(message.getSender().getId(), userSender.getId());
        Assertions.assertEquals(message.getReceiver().getId(), userReceiver.getId());
        Assertions.assertEquals(message.getContent(), "Hello World!");

        message = message.setRead(true);
        messageRepository.save(message);
        Assertions.assertEquals(message.getRead(), true);

        message = message.setRead(false);
        messageRepository.save(message);
        Assertions.assertEquals(message.getRead(), false);

        message = message.setContent("How are you?");
        messageRepository.save(message);
        Assertions.assertEquals(message.getContent(), "How are you?");

        message = message.setSender(userReceiver);
        message = message.setReceiver(userSender);
        messageRepository.save(message);
        Assertions.assertEquals(message.getSender().getId(), userReceiver.getId());
        Assertions.assertEquals(message.getReceiver().getId(), userSender.getId());

        message = message.setTimestamp(Timestamp.valueOf("1111-11-11 11:11:11"));
        messageRepository.save(message);
        Assertions.assertEquals(message.getTimestamp(), Timestamp.valueOf("1111-11-11 11:11:11"));

        Long newId = message.getId() + 1;
        message.setId(newId);
        messageRepository.save(message);
        Assertions.assertEquals(message.getId(), newId);
        message.setId(newId - 1);
        messageRepository.save(message);
    }

    @Test
    void reportBuddy() {
        Course course = createExpectedCourse();
        User userReporter = createExpectedUser();
        User userReported = createExpectedBuddy();
        Long userReporterId = userRepository.save(userReporter).getId();
        Long userReportedId = userRepository.save(userReported).getId();
        course.addNewUser(userReporter);
        course.addNewUser(userReported);
        courseRepository.save(course);

        reportedBuddiesRepository.addReportedBuddy(userReporterId, userReportedId, "Bad buddy");
        ReportedBuddies reportedBuddies = reportedBuddiesRepository.getById(1L);
        Long reportedBuddiesId = reportedBuddiesRepository.save(reportedBuddies).getId();
        reportedBuddiesRepository.save(reportedBuddies);
        Assertions.assertEquals(reportedBuddiesId, reportedBuddies.getId());
        Assertions.assertEquals(reportedBuddies.getUserReporter().getId(), userReporter.getId());
        Assertions.assertEquals(reportedBuddies.getUserReported().getId(), userReported.getId());
        Assertions.assertEquals(reportedBuddies.getReportInformation(), "Bad buddy");

        reportedBuddies = reportedBuddies.setUserReporter(userReported);
        reportedBuddies = reportedBuddies.setUserReported(userReporter);
        reportedBuddiesRepository.save(reportedBuddies);
        Assertions.assertEquals(reportedBuddies.getUserReporter().getId(), userReported.getId());
        Assertions.assertEquals(reportedBuddies.getUserReported().getId(), userReporter.getId());

        reportedBuddies.setIsReportRead(true);
        reportedBuddiesRepository.save(reportedBuddies);
        Assertions.assertEquals(reportedBuddies.getIsReportRead(), true);

        reportedBuddies.setIsReportResolved(true);
        reportedBuddiesRepository.save(reportedBuddies);
        Assertions.assertEquals(reportedBuddies.getIsReportResolved(), true);

        reportedBuddies.setReportInformation("finn");
        reportedBuddiesRepository.save(reportedBuddies);
        Assertions.assertEquals(reportedBuddies.getReportInformation(), "finn");

        Long newId = reportedBuddies.getId() + 1;
        reportedBuddies = reportedBuddies.setId(newId);
        reportedBuddiesRepository.save(reportedBuddies);
        Assertions.assertEquals(reportedBuddies.getId(), newId);
        reportedBuddies = reportedBuddies.setId(newId - 1);
        reportedBuddiesRepository.save(reportedBuddies);
    }

    @AfterEach
    public void clear() {
        courseRepository.deleteAll();
        buddiesRepository.deleteAll();
        blockedBuddiesRepository.deleteAll();
        messageRepository.deleteAll();
        reportedBuddiesRepository.deleteAll();
        userRepository.deleteAll();
    }

    User createExpectedUser() {
        return new User()
                .setName("John Test")
                .setEmail("john.test@example.com");
    }

    User createExpectedBuddy() {
        return new User()
                .setName("Expected Buddy")
                .setEmail("expected.buddy@example.com");
    }

    Course createExpectedCourse() {
        Course course = new Course();
        course.setName("se701");
        course.setSemester("2022 Semester 1");
        course.setUpdatedTime(Timestamp.from(Instant.now()));
        return course;
    }
}
