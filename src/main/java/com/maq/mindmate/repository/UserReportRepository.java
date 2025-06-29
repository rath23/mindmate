package com.maq.mindmate.repository;

import com.maq.mindmate.models.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
    int countByReportedNicknameAndRoom(String reportedNickname, String room);
}