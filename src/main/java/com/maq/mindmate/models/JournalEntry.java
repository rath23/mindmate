package com.maq.mindmate.models;

import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "journal_entries")
@Data
public class JournalEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String heading;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String body; // optional

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
