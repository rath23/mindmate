package com.maq.mindmate.services;

import com.maq.mindmate.dto.BadgeDTO;
import com.maq.mindmate.dto.UserProgressResponse;
import com.maq.mindmate.exceptions.UserNotFoundException;
import com.maq.mindmate.models.*;
import com.maq.mindmate.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class XPTrackingService {

    @Autowired
    private final MoodEntryRepository moodEntryRepository;
    @Autowired
    private final JournalEntryRepository journalEntryRepository;
    @Autowired
    private  final  UserService userService;
    @Autowired
    private final DailyTaskRepository dailyTaskRepository;
    @Autowired
    private final BadgeRepository badgeRepository;

    @Autowired
    private UserBadgeRepository userBadgeRepository;

    @Autowired
    private BadgeService badgeService;

    public int calculateXP(User user) {
try{
        int xp = 0;

        // +10 XP per mood check-in
        int moodCount = moodEntryRepository.countByUserId(user.getId());
        xp += moodCount * 10;

        // +20 XP per journal entry
        int journalCount = journalEntryRepository.countByUserId(user.getId());
        xp += journalCount * 20;

        // +5 XP per day in streak
        int streak = calculateStreak(user);
        xp += streak * 5;

        return xp;
} catch (DataAccessException ex) {
    throw new RuntimeException("Error calculating XP: " + ex.getMessage());
}
    }

    public int calculateStreak( User user) {
        try{
        List<MoodEntry> entries = moodEntryRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        int streak = 0;
        LocalDate today = LocalDate.now();

        for (int i = 0; i < 30; i++) {
            LocalDate date = today.minusDays(i);
            boolean hasEntry = entries.stream()
                    .anyMatch(e -> e.getCreatedAt().toLocalDate().isEqual(date));

            if (hasEntry) {
                streak++;
            } else {
                break;
            }
        }
        return streak;
        } catch (DataAccessException ex) {
            throw new RuntimeException("Error calculating streak: " + ex.getMessage());
        }
    }

//    public UserProgressResponse getUserProgress(Authentication authentication) {
//        User user = userService.getCurrentUserDetailWithAuth(authentication);
//        int streak = calculateStreak(user);
//        int xp = calculateXP(user);
//        List<DailyTask> todayTasks = dailyTaskRepository.findByUserAndAssignedDate(user, LocalDate.now());
//        List<BadgeDTO> badges = badgeService.getUserBadges(user);
//
//        return new UserProgressResponse(xp,streak, todayTasks, badges);
//    }
    public UserProgressResponse getUserProgress(Authentication authentication) {
        try {
            User user = userService.getCurrentUserDetailWithAuth(authentication);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            int streak = calculateStreak(user);
            int xp = calculateXP(user);

            List<DailyTask> todayTasks;
            try {
                todayTasks = dailyTaskRepository.findByUserAndAssignedDate(user, LocalDate.now());
            } catch (DataAccessException e) {
                todayTasks = Collections.emptyList();
            }

            List<BadgeDTO> badges;
            try {
                badges = badgeService.getUserBadges(user);
            } catch (Exception e) {
                badges = Collections.emptyList();
            }

            return new UserProgressResponse(xp, streak, todayTasks, badges);
        } catch (UserNotFoundException | DataAccessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user progress: " + e.getMessage());
        }
    }
}
