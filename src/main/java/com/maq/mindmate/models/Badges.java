package com.maq.mindmate.models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "badges")
@Data
//@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Badges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "color_start", nullable = false)
    private String colorStart;

    @Column(name = "color_end", nullable = false)
    private String colorEnd;

    @Column(nullable = false)
    private boolean unlocked = false;


}