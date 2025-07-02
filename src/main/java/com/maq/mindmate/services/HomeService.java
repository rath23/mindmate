package com.maq.mindmate.services;

import com.maq.mindmate.dto.HomeDTO;
import com.maq.mindmate.enums.MoodType;
import com.maq.mindmate.models.MoodEntry;
import com.maq.mindmate.models.User;
import com.maq.mindmate.repository.MoodEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class HomeService {

    @Autowired
    private  XPTrackingService xpService;

    @Autowired
    private  MoodEntryRepository moodEntryRepo;



    public ResponseEntity<HomeDTO> getHomeData(User user) {

        int xp = xpService.calculateXP(user);
        int streak = xpService.calculateStreak(user);

        // Get today’s mood if available
        LocalDate today = LocalDate.now();
        Optional<MoodEntry> todayEntryOpt = moodEntryRepo.findFirstByUserIdAndCreatedAtBetween(
                user.getId(),
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );

        MoodType todayMood = todayEntryOpt.map(MoodEntry::getMood).orElse(null);


        return ResponseEntity.ok(new HomeDTO(xp, streak, todayMood));
    }
}
