package com.maq.mindmate.services;

import com.maq.mindmate.models.User;
import com.maq.mindmate.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DailyTaskScheduler {

    private final UserRepo userRepository;
    private final GeminiTaskService taskService;

        @Scheduled(cron = "0 0 0 * * *") // every midnight
public void generateDailyTasks() {
        List<User> users = userRepository.findAll();
        users.forEach(taskService::generateTasksIfNotExist);
    }
}

