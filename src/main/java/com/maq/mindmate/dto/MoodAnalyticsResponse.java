package com.maq.mindmate.dto;

import com.maq.mindmate.enums.MoodType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class MoodAnalyticsResponse {
    private Map<MoodType, Long> moodCounts;
    private Map<String, Long> topTags;
    private int currentStreakDays;
    private List<DailyMoodDTO> last15Days;
}
