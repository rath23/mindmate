package com.maq.mindmate.dto;

import com.maq.mindmate.enums.MoodType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class HomeDTO {
    private int xp;
    private int streak;
    private MoodType todayMood;
    private List<Map<String, String>> suggestions;
}
