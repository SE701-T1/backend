package com.team701.buddymatcher.dtos.timetable;

import java.sql.Timestamp;

public class CourseDTO {

    private Long courseId;
    private String name;
    private String semester;
    private Integer studentCount;
    private Integer buddyCount;
    private Timestamp updatedTime;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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

    public Integer getBuddyCount() {
        return buddyCount;
    }

    public void setBuddyCount(Integer buddyCount) {
        this.buddyCount = buddyCount;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }
}
