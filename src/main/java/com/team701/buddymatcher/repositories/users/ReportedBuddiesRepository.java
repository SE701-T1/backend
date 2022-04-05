package com.team701.buddymatcher.repositories.users;

import com.team701.buddymatcher.domain.users.ReportedBuddies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

/**
 * Repository class for performing operations on the ReportedBuddies object
 */
public interface ReportedBuddiesRepository extends JpaRepository<ReportedBuddies, Long>,
        JpaSpecificationExecutor<ReportedBuddies> {

    /**
     * Add a new reporting user and reported user pair to the REPORTED_BUDDIES database table with report information
     * The order of users entered into the table is: (USER_REPORTER_ID, USER_REPORTED_ID) followed by REPORT_INFO
     * @param userReporterId the user ID of the user reporting the buddy user
     * @param userReportedId the user ID of the buddy user being reported
     * @param reportInfo the report information given by the reporting user
     */
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO Reported_Buddies (user_reporter_id, user_reported_id, report_info) VALUES " +
            "(:userReporterId, :userReportedId, :reportInfo)",
            nativeQuery=true)
    void addReportedBuddy(@Param("userReporterId") Long userReporterId,
                          @Param("userReportedId") Long userReportedId,
                          @Param("reportInfo") String reportInfo);
}
