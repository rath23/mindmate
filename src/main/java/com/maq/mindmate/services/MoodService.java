package com.maq.mindmate.services;

import com.maq.mindmate.dto.DailyMoodDTO;
import com.maq.mindmate.dto.MoodAnalyticsResponse;
import com.maq.mindmate.dto.MoodEntryRequest;
import com.maq.mindmate.enums.MoodType;
import com.maq.mindmate.models.MoodEntry;
import com.maq.mindmate.models.User;
import com.maq.mindmate.repository.MoodEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.TreeMap;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoodService {

    @Autowired
    private final MoodEntryRepository moodEntryRepository;

    @Autowired
    private final UserService userService;


    public MoodEntry saveMoodEntry(MoodEntryRequest request, Authentication authentication) {
        User user = userService.getCurrentUserDetailWithAuth(authentication);
        MoodEntry moodEntry = new MoodEntry();
        moodEntry.setMood(request.getMood());
        moodEntry.setTags(request.getTags());
        moodEntry.setNote(request.getNote());
        moodEntry.setCreatedAt(LocalDateTime.now());
        moodEntry.setUser(user);

        return moodEntryRepository.save(moodEntry);
    }

    public List<MoodEntryRequest> getMoodHistory(Authentication authentication) {
        User user = userService.getCurrentUserDetailWithAuth(authentication);

        List<MoodEntry> entries = moodEntryRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());

        return entries.stream()
                .map(entry -> {
                    // access inside the session to ensure it's loaded
                    entry.getTags().size();

                    return MoodEntryRequest.builder()
                            .id(entry.getId())
                            .mood(entry.getMood())
                            .tags(entry.getTags()) // safe now
                            .note(entry.getNote())
                            .createdAt(entry.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());

    }

    public MoodAnalyticsResponse analyzeMoodData(Authentication authentication) {
        User user = userService.getCurrentUserDetailWithAuth(authentication);
        List<MoodEntry> entries = moodEntryRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId());

        // Mood count map
        Map<MoodType, Long> moodCounts = entries.stream()
                .collect(Collectors.groupingBy(MoodEntry::getMood, Collectors.counting()));

        // Tag frequency map
        Map<String, Long> tagCounts = entries.stream()
                .flatMap(e -> e.getTags().stream())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Last 15 days mood mapping
        Map<LocalDate, MoodType> moodByDate = entries.stream()
                .collect(Collectors.toMap(
                        e -> e.getCreatedAt().toLocalDate(),
                        MoodEntry::getMood,
                        (existing, replacement) -> existing, // keep first of the day
                        TreeMap::new
                ));

        List<DailyMoodDTO> last15Days = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 14; i >= 0; i--) {  // Changed from 6 to 14
            LocalDate date = today.minusDays(i);
            MoodType mood = moodByDate.getOrDefault(date, null);
            last15Days.add(new DailyMoodDTO(date, mood));
        }

        // Calculate streak
        int streak = 0;
        for (int i = 0; i < 30; i++) {
            LocalDate date = today.minusDays(i);
            if (moodByDate.containsKey(date)) {
                streak++;
            } else {
                break;
            }
        }

        return new MoodAnalyticsResponse(moodCounts, tagCounts, streak, last15Days);
    }






}

