package com.team701.buddymatcher.domain.timetable;

import com.team701.buddymatcher.domain.users.User;

import javax.persistence.*;
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
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "COURSE_ID"))
    private Set<User> users = new HashSet<>();

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long id) {
        this.courseId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    public void addNewUser(User user){
        if (!users.contains(user)){
            users.add(user);
            user.getCourses().add(this);
            studentCount++;
        }
    }
}
