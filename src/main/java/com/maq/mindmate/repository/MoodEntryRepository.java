package com.maq.mindmate.repository;

import com.maq.mindmate.models.MoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MoodEntryRepository extends JpaRepository<MoodEntry, UUID> {
    List<MoodEntry> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
