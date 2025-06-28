package com.maq.mindmate.services;

import com.maq.mindmate.dto.MoodEntryRequest;
import com.maq.mindmate.models.MoodEntry;
import com.maq.mindmate.models.User;
import com.maq.mindmate.repository.MoodEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MoodService {

    @Autowired
    private final MoodEntryRepository moodEntryRepository;

    @Autowired
    private final UserService userService;


    public MoodEntry saveMoodEntry(MoodEntryRequest request, Authentication authentication) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(userDetails);
        MoodEntry moodEntry = new MoodEntry();
        moodEntry.setMood(request.getMood());
        moodEntry.setTags(request.getTags());
        moodEntry.setNote(request.getNote());
        moodEntry.setCreatedAt(LocalDateTime.now());
        moodEntry.setUser(user);

        return moodEntryRepository.save(moodEntry);
    }

    public List<MoodEntryRequest> getMoodHistory(Authentication authentication) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(userDetails);

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


}

