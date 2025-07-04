package com.maq.mindmate.services;

import com.maq.mindmate.exceptions.TaskNotFoundException;
import com.maq.mindmate.models.DailyTask;
import com.maq.mindmate.models.User;
import com.maq.mindmate.repository.DailyTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeminiTaskService {

    private final DailyTaskRepository dailyTaskRepo;
    private final GeminiService geminiService;



    public void generateTasksIfNotExist(User user) {
        LocalDate today = LocalDate.now();
        List<DailyTask> existing = dailyTaskRepo.findByUserAndAssignedDate(user, today);
        if (!existing.isEmpty()) return;

        List<String> tasks = geminiService.generateDailyTasks(user.getUserName());

        for (String t : tasks) {
            dailyTaskRepo.save(new DailyTask(null, t, false, today, user));
        }
    }

    public List<DailyTask> getTodayTasks(User user) {
        return dailyTaskRepo.findByUserAndAssignedDate(user, LocalDate.now());
    }

    public void markTaskComplete(Long taskId, User user) {
        DailyTask task = dailyTaskRepo.findById(taskId)
                .filter(t -> t.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        task.setCompleted(true);
        dailyTaskRepo.save(task);
    }
}
