package com.maq.mindmate.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String room;
    private String senderNickname;
    private String content;
    private LocalDateTime timestamp = LocalDateTime.now();
}
