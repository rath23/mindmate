package com.maq.mindmate.services;

import com.maq.mindmate.exceptions.BlockDurationExceededException;
import com.maq.mindmate.exceptions.SelfReportingException;
import com.maq.mindmate.models.BlockedUser;
import com.maq.mindmate.models.UserReport;
import com.maq.mindmate.repository.BlockedUserRepository;
import com.maq.mindmate.repository.UserReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ReportService {
    @Autowired
    private final UserReportRepository reportRepository;
    @Autowired
    private final BlockedUserRepository blockedUserRepository;
    private static final int[] BLOCK_DURATIONS = {2, 6, 10, -1};

    @Transactional
    public void handleReport(String reporter, String reported, String room) {
        // Save report
        UserReport report = new UserReport();
        if (reporter.equalsIgnoreCase(reported)) {
            throw new SelfReportingException("Users cannot report themselves.");
        }

        report.setReporterNickname(reporter);
        report.setReportedNickname(reported);
        report.setRoom(room);
        reportRepository.save(report);

        // Check report count
        int reportCount = reportRepository.countByReportedNicknameAndRoom(reported, room);
        if (reportCount >= 10) {
            applyBlock(reported, room, reportCount / 10);
        }
    }

    private void applyBlock(String nickname, String room, int violationLevel) {

        if (violationLevel > BLOCK_DURATIONS.length) {
            throw new BlockDurationExceededException("Violation level exceeds defined limits.");
        }

        BlockedUser block = blockedUserRepository.findByNicknameAndRoom(nickname, room)
                .orElse(new BlockedUser());

        block.setNickname(nickname);
        block.setRoom(room);
        block.setViolationCount(violationLevel);

        if (BLOCK_DURATIONS[violationLevel - 1] == -1) {
            block.setBlockedUntil(LocalDateTime.MAX); // Permanent
        } else {
            block.setBlockedUntil(LocalDateTime.now().plusDays(
                    BLOCK_DURATIONS[violationLevel - 1]
            ));
        }

        blockedUserRepository.save(block);
    }
}
