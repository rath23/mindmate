package com.maq.mindmate.dto;

import com.maq.mindmate.models.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class JournalDTO {


    private UUID id;

    private String heading;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String body; // optional

}
