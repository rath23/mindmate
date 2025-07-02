package com.maq.mindmate.dto;

import com.maq.mindmate.enums.MoodType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyMoodDTO {
    private LocalDate date;
    private MoodType mood;
}
