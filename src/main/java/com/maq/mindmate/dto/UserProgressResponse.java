package com.maq.mindmate.dto;


import com.maq.mindmate.models.Badges;
import com.maq.mindmate.models.DailyTask;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserProgressResponse {
    private int xp;
    private int streak;
    private List<DailyTask> dailyTasks;
    private List<BadgeDTO> unlockedBadges;
}
