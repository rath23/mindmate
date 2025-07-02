package com.maq.mindmate.services;

import com.maq.mindmate.dto.BadgeDTO;
import com.maq.mindmate.models.*;
import com.maq.mindmate.repository.BadgeRepository;
import com.maq.mindmate.repository.JournalEntryRepository;
import com.maq.mindmate.repository.MoodEntryRepository;
import com.maq.mindmate.repository.UserBadgeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final BadgeRepository badgeRepo;
    private final UserBadgeRepository userBadgeRepo;
    private final MoodEntryRepository moodRepo;
    private final JournalEntryRepository journalRepo;

    public void evaluateAndAssignBadges(User user) {
        List<MoodEntry> moods = moodRepo.findAllByUserId(user.getId());
        List<String> moodTypes = moods.stream().map(m -> m.getMood().name()).distinct().toList();
        long journalCount = journalRepo.countByUserId(user.getId());

        int streak = calculateStreak(user);

        // Badge conditions
        unlock("Mood Rookie", user, moods.size() >= 1);
        unlock("Streak Starter", user, streak >= 3);
        unlock("Feelings Pro", user, moods.size() >= 30);
        unlock("Emotional Explorer", user, moodTypes.size() >= 5);

        unlock("Reflective Soul", user, journalCount >= 1);
        unlock("Insight Seeker", user, journalCount >= 7);
        unlock("Daily Writer", user, checkJournalStreak(user));

        unlock("7-Day Streak", user, streak >= 7);
        unlock("14-Day Warrior", user, streak >= 14);
        unlock("30-Day MindMate", user, streak >= 30);
    }

    private boolean checkJournalStreak(User user) {
        List<JournalEntry> entries = journalRepo.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 5; i++) {
            LocalDate targetDate = today.minusDays(i);
            boolean exists = entries.stream().anyMatch(e -> e.getCreatedAt().toLocalDate().equals(targetDate));
            if (!exists) return false;
        }
        return true;
    }

    private int calculateStreak(User user) {
        List<MoodEntry> moods = moodRepo.findAllByUserIdOrderByCreatedAtDesc(user.getId());
        int streak = 0;
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 30; i++) {
            LocalDate date = today.minusDays(i);
            boolean found = moods.stream().anyMatch(e -> e.getCreatedAt().toLocalDate().isEqual(date));
            if (found) streak++;
            else break;
        }
        return streak;
    }

    private void unlock(String badgeName, User user, boolean condition) {
        if (!condition) return;
        Badges badge = badgeRepo.findByName(badgeName);
        if (badge == null) {
            throw new EntityNotFoundException("Badge not found: " + badgeName);
        }
        if (!userBadgeRepo.existsByUserAndBadge(user, badge)) {
            userBadgeRepo.save(new UserBadge(null, user, badge, LocalDate.now()));
        }
    }

    public List<BadgeDTO> getUserBadges(User user) {
        List<UserBadge> unlocked = userBadgeRepo.findByUser(user);
        Set<Long> unlockedIds = unlocked.stream().map(ub -> ub.getBadge().getId()).collect(Collectors.toSet());

        return badgeRepo.findAll().stream().map(badge -> new BadgeDTO(
                badge.getId(),
                badge.getName(),
                badge.getDescription(),
                badge.getColorStart(),
                badge.getColorEnd(),
                unlockedIds.contains(badge.getId())
        )).toList();
    }
}

