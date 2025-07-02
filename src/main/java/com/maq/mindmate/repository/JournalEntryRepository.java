package com.maq.mindmate.repository;

import com.maq.mindmate.models.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, UUID> {
    List<JournalEntry> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<JournalEntry>  findAllByUserIdOrderByCreatedAtDesc(UUID id);
    int countByUserId(UUID userId);
}

