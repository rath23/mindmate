package com.maq.mindmate.services;

import com.maq.mindmate.dto.BadgeDTO;
import com.maq.mindmate.dto.UserProgressResponse;
import com.maq.mindmate.models.*;
import com.maq.mindmate.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    }

    public int calculateStreak( User user) {
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
    }

    public UserProgressResponse getUserProgress(Authentication authentication) {
        User user = userService.getCurrentUserDetailWithAuth(authentication);
        int streak = calculateStreak(user);
        int xp = calculateXP(user);
        List<DailyTask> todayTasks = dailyTaskRepository.findByUserAndAssignedDate(user, LocalDate.now());
        List<BadgeDTO> badges = badgeService.getUserBadges(user);

        return new UserProgressResponse(xp,streak, todayTasks, badges);
    }
}
