package com.maq.mindmate.models;

import com.maq.mindmate.enums.MoodType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "mood_entries")
@Data
public class MoodEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private MoodType mood;


//    @ElementCollection
//    @CollectionTable(name = "mood_entry_tags", joinColumns = @JoinColumn(name = "mood_entry_id"))
//    @Column(name = "tags")
//    @BatchSize(size = 20) // Load tags in batches of 20 entries
//    private List<String> tags = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> tags;

    @Column(columnDefinition = "TEXT")
    private String note;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
