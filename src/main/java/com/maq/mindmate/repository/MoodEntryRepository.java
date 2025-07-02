package com.maq.mindmate.repository;

import com.maq.mindmate.models.MoodEntry;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MoodEntryRepository extends JpaRepository<MoodEntry, UUID> {
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<MoodEntry> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
    List<MoodEntry> findAllByUserId(UUID id);
    int countByUserId(UUID userId);
    Optional<MoodEntry> findFirstByUserIdAndCreatedAtBetween(UUID userId, LocalDateTime start, LocalDateTime end);

}
