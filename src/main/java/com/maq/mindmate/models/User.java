package com.maq.mindmate.models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "users")
@Data
//@Cacheable
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
