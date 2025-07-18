package com.maq.mindmate.services;

import com.maq.mindmate.dto.HomeDTO;
import com.maq.mindmate.enums.MoodType;
import com.maq.mindmate.exceptions.AISuggestionException;
import com.maq.mindmate.models.MoodEntry;
import com.maq.mindmate.models.User;
import com.maq.mindmate.repository.MoodEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HomeService {

    @Autowired
    private  XPTrackingService xpService;

    @Autowired
    private  MoodEntryRepository moodEntryRepo;

    @Autowired
    private SelfCareAIService selfCareAIService;



    public ResponseEntity<HomeDTO> getHomeData(User user) {

        int xp = xpService.calculateXP(user);
        int streak = xpService.calculateStreak(user);

        // Today’s mood
        LocalDate today = LocalDate.now();
        Optional<MoodEntry> todayEntryOpt = moodEntryRepo.findFirstByUserIdAndCreatedAtBetween(
                user.getId(),
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );
        MoodType todayMood = todayEntryOpt.map(MoodEntry::getMood).orElse(null);

        // AI suggestions
        List<Map<String, String>> suggestions = new ArrayList<>();
        ResponseEntity<?> aiResponse = selfCareAIService.getAISuggestions(user);

        if (!aiResponse.getStatusCode().is2xxSuccessful()) {
            throw new AISuggestionException("AI service failed to return suggestions.");
        }

        Object body = aiResponse.getBody();
        if (body instanceof Map<?, ?> bodyMap) {
            Object suggestionsObj = bodyMap.get("suggestions");
            if (suggestionsObj instanceof List<?> suggestionList) {
                for (Object item : suggestionList) {
                    if (item instanceof Map<?, ?> map) {
                        suggestions.add((Map<String, String>) map);
                    }
                }
            } else {
                throw new AISuggestionException("Invalid suggestions format from AI service.");
            }
        } else {
            throw new AISuggestionException("Invalid response from AI service.");
        }

        return ResponseEntity.ok(new HomeDTO(xp, streak, todayMood, suggestions));
    }

}
