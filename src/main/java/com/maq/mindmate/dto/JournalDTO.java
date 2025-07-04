package com.maq.mindmate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class JournalDTO {

    private UUID id;

    @NotBlank(message = "Heading is required")
    @Size(max = 150, message = "Heading must not exceed 150 characters")
    private String heading;

//    @PastOrPresent(message = "Created time cannot be in the future")
    private LocalDateTime createdAt;

//    @PastOrPresent(message = "Updated time cannot be in the future")
    private LocalDateTime updatedAt;

    @NotBlank(message = "Body is required")
    @Size(max = 2000, message = "Body must not exceed 2000 characters")
    private String body;
}
