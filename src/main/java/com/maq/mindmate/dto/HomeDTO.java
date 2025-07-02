package com.maq.mindmate.dto;

import com.maq.mindmate.enums.MoodType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HomeDTO {
    private int xp;
    private int streak;
    private MoodType todayMood;
}
