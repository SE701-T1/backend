package com.team701.buddymatcher.domain.users;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.Column;

/**
 * Entity model class for REPORTED_BUDDIES database table
 * The order of users entered into the table is: (USER_REPORTER_ID, USER_REPORTED_ID)
 */
@Entity
@Table(name = "REPORTED_BUDDIES")
public class ReportedBuddies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_REPORTER_ID")
    private User userReporter;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_REPORTED_ID")
    private User userReported;

    @Column(name = "REPORT_INFO", nullable = false)
    private String reportInformation;

    @Column(name = "IS_REPORT_READ", columnDefinition = "boolean default false")
    private Boolean isReportRead;

    @Column(name = "IS_REPORT_RESOLVED", columnDefinition = "boolean default false")
    private Boolean isReportResolved;

    /**
     * Getter method for class instance ID, or ID for the row in the REPORTED_BUDDIES database table
     * @return the class instance ID, or ID for the row in the REPORTED_BUDDIES database table
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Setter method for setting the class instance ID, or ID for the row in the REPORTED_BUDDIES database table
     * @param id the class instance ID, or ID for the row in the REPORTED_BUDDIES database table
     * @return the ReportedBuddies class instance
     */
    public ReportedBuddies setId(Long id) {
        this.id = id;
        return this;
    }

    /**
     * Getter method for the reporting User class instance
     * @return the reporting User class instance
     */
    public User getUserReporter() {
        return this.userReporter;
    }

    /**
     * Setter method for setting the reporting User class instance
     * @param userReporter reporting User class instance
     * @return the ReportedBuddies class instance
     */
    public ReportedBuddies setUserReporter(User userReporter) {
        this.userReporter = userReporter;
        return this;
    }

    /**
     * Getter method for the reported User class instance
     * @return the reported User class instance
     */
    public User getUserReported() {
        return this.userReported;
    }

    /**
     * Setter method for setting the reported User class instance
     * @param userReported reported User class instance
     * @return the ReportedBuddies class instance
     */
    public ReportedBuddies setUserReported(User userReported) {
        this.userReported = userReported;
        return this;
    }

    /**
     * Getter method for the report information
     * @return the report information
     */
    public String getReportInformation() {
        return this.reportInformation;
    }

    /**
     * Setter method for setting the reported information
     * @param reportInformation the report information
     */
    public void setReportInformation(String reportInformation) {
        this.reportInformation = reportInformation;
    }

    /**
     * Getter method for the boolean for if the report is read or not
     * @return boolean for if the report is read or not
     */
    public Boolean getIsReportRead() {
        return this.isReportRead;
    }

    /**
     * Setter method for setting the boolean for if the report is read or not
     * @param isReportRead boolean representing if the report has been read or not
     */
    public void setIsReportRead(Boolean isReportRead) {
        this.isReportRead = isReportRead;
    }

    /**
     * Getter method for the boolean for if the report is resolved or not
     * @return boolean for if the report is resolved or not
     */
    public Boolean getIsReportResolved() {
        return this.isReportResolved;
    }

    /**
     * Setter method for setting the boolean for if the report is resolved or not
     * @param isReportResolved boolean representing if the report has been resolved or not
     */
    public void setIsReportResolved(Boolean isReportResolved) {
        this.isReportResolved = isReportResolved;
    }
}
