package com.team701.buddymatcher.dtos.users;

import java.io.Serializable;
import java.util.Objects;

/**
 * Data transfer object for the ReportedBuddies entity
 */
public class ReportedBuddiesDTO implements Serializable {
    private final Long id;
    private final UserDTO userReporter;
    private final UserDTO userReported;
    private final String reportInformation;
    private final Boolean isReportRead;
    private final Boolean isReportResolved;

    public ReportedBuddiesDTO(Long id,
                              UserDTO userReporter,
                              UserDTO userReported,
                              String reportInformation,
                              Boolean isReportRead,
                              Boolean isReportResolved) {
        this.id = id;
        this.userReporter = userReporter;
        this.userReported = userReported;
        this.reportInformation = reportInformation;
        this.isReportRead = isReportRead;
        this.isReportResolved = isReportResolved;
    }

    public Long getId() {
        return this.id;
    }

    public UserDTO getUserReporter() {
        return this.userReporter;
    }

    public UserDTO getUserReported() {
        return this.userReported;
    }

    public String getReportInformation() {
        return this.reportInformation;
    }

    public Boolean getIsReportRead() {
        return this.isReportRead;
    }

    public Boolean getIsReportResolved() {
        return this.isReportResolved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportedBuddiesDTO entity = (ReportedBuddiesDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.userReporter, entity.userReporter) &&
                Objects.equals(this.userReported, entity.userReported) &&
                Objects.equals(this.reportInformation, entity.reportInformation) &&
                Objects.equals(this.isReportRead, entity.isReportRead) &&
                Objects.equals(this.isReportResolved, entity.isReportResolved);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userReporter, userReported, reportInformation, isReportRead, isReportResolved);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "userReporter = " + userReporter + ", " +
                "userReported = " + userReported + ", " +
                "reportInformation = " + reportInformation + ", " +
                "isReportRead = " + isReportRead + ", " +
                "isReportResolved = " + isReportResolved + ")";
    }
}
