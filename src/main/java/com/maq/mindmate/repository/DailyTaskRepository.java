package com.maq.mindmate.repository;

import com.maq.mindmate.models.DailyTask;
import com.maq.mindmate.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DailyTaskRepository extends JpaRepository<DailyTask, Long> {
    List<DailyTask> findByUserAndAssignedDate(User user, LocalDate date);
}

