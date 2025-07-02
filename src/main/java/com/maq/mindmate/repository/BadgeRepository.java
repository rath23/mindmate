package com.maq.mindmate.repository;

import com.maq.mindmate.models.Badges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badges,Long> {
    Badges findByName(String name);
}
