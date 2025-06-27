package com.maq.mindmate.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true)
    private String email;

    private String password;

    private String userName;

    private String name;

    private String nickName;

    private Boolean anonymousMode;

    private Boolean reminderEnabled;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    private Boolean isDeleted;
}
