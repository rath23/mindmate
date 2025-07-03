package com.maq.mindmate.controllers;

import com.maq.mindmate.dto.MoodAnalyticsResponse;
import com.maq.mindmate.dto.MoodEntryRequest;
import com.maq.mindmate.models.MoodEntry;

import com.maq.mindmate.models.User;
import com.maq.mindmate.services.MoodService;

import com.maq.mindmate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/mood")
@RequiredArgsConstructor
public class MoodController {

    @Autowired
    private final MoodService moodService;

    @Autowired
    private UserService userService;



    @PostMapping
    public ResponseEntity<?> checkInMood(@RequestBody MoodEntryRequest request, Authentication authentication) {
        User user = userService.getCurrentUserDetailWithAuth(authentication);
        MoodEntry saved = moodService.saveMoodEntry(request, user);
        return ResponseEntity.ok("Mood saved successfully at " + saved.getCreatedAt());
    }

    @GetMapping("/history")
    public ResponseEntity<?> getMoodHistory(Authentication authentication) {
        User user = userService.getCurrentUserDetailWithAuth(authentication);
        return ResponseEntity.ok(moodService.getMoodHistory(user));
    }
    @GetMapping("/analytics")
    public ResponseEntity<?> getMoodAnalytics(Authentication authentication) {
        User user = userService.getCurrentUserDetailWithAuth(authentication);
        return ResponseEntity.ok(moodService.analyzeMoodData(user));
    }

}
