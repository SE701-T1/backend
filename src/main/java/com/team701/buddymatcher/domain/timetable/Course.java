package com.team701.buddymatcher.domain.timetable;

import com.team701.buddymatcher.domain.users.User;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "COURSE")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COURSE_ID")
    private long courseId;

    @Column(name = "COURSE_NAME", nullable = false)
    private String name;

    @Column(name = "SEMESTER", nullable = false)
    private String semester;

    @Column(name = "STUDENT_COUNT")
    private Integer studentCount = 0;

    @Column(name = "TIME_UPDATED")
    private Timestamp updatedTime;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "COURSE_STUDENT",
            joinColumns = @JoinColumn(name = "COURSE_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private Set<User> users = new HashSet<>();

    public Set<User> getUsers() {
        return users;
    }

    public Course setUsers(Set<User> users) {
        this.users = users;
        return this;
    }

    public long getCourseId() {
        return courseId;
    }

    public Course setCourseId(long id) {
        this.courseId = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Course setName(String name) {
        this.name = name;
        return this;
    }

    public String getSemester() {
        return semester;
    }

    public Course setSemester(String semester) {
        this.semester = semester;
        return this;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public Course setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
        return this;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public Course setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    public Course addNewUser(User user){
        if (!users.contains(user)){
            users.add(user);
            user.getCourses().add(this);
            studentCount++;
        }
        return this;
    }

    /**
     * Remove a user from course enrollment
     * @param user the user being un-enrolled from the course
     * @return the course without the user being enrolled
     */
    public Course removeUser(User user) {
        if (this.users.contains(user)) {
            users.remove(user);
            user.getCourses().remove(this);
            studentCount--;
        }
        return this;
    }
}
