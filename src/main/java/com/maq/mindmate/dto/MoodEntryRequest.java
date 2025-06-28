package com.maq.mindmate.dto;

import com.maq.mindmate.enums.MoodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class MoodEntryRequest {
    private UUID id;
    private MoodType mood;
    private List<String> tags;
    private String note;
    private LocalDateTime createdAt;
}

