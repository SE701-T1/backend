package com.team701.buddymatcher.dtos.pairing;

import java.util.List;

/**
 * Data Transfer Object representing a request to find a buddy match from a given list of courses
 */
public class MatchBuddyDTO {

    private List<Long> courseIds;

    public List<Long> getCourseIds() {
        return this.courseIds;
    }

    public void setCourseIds(List<Long> courseIds) {
        this.courseIds = courseIds;
    }
}
