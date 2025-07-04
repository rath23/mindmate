package com.maq.mindmate.dto;

import com.maq.mindmate.enums.MoodType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
public class MoodEntryRequest {

    private UUID id;

    @NotNull(message = "Mood must not be null")
    private MoodType mood;

    // Optional: ensure tags are not too many
    @Size(max = 10, message = "You can add at most 10 tags")
    private List<@NotBlank(message = "Tags must not be blank") String> tags;

    @Size(max = 500, message = "Note cannot exceed 500 characters")
    private String note;

    // Optional: ensure createdAt is not a future date
    @PastOrPresent(message = "Created date cannot be in the future")
    private LocalDateTime createdAt;
}
