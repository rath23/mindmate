package com.maq.mindmate.controllers;

import com.maq.mindmate.dto.MoodEntryRequest;
import com.maq.mindmate.models.MoodEntry;
import com.maq.mindmate.models.User;
import com.maq.mindmate.services.MoodService;
import com.maq.mindmate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mood")
@RequiredArgsConstructor
public class MoodController {

    @Autowired
    private final MoodService moodService;



    @PostMapping
    public ResponseEntity<?> checkInMood(@RequestBody MoodEntryRequest request, Authentication authentication) {
        MoodEntry saved = moodService.saveMoodEntry(request, authentication);
        return ResponseEntity.ok("Mood saved successfully at " + saved.getCreatedAt());
    }

    @GetMapping("/history")
    public ResponseEntity<?> getMoodHistory(Authentication authentication) {
        System.out.println("before return");
        return ResponseEntity.ok(moodService.getMoodHistory(authentication));
    }
}
