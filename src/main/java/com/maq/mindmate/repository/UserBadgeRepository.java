package com.maq.mindmate.repository;

import com.maq.mindmate.models.Badges;
import com.maq.mindmate.models.User;
import com.maq.mindmate.models.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    boolean existsByUserAndBadge(User user, Badges badge);
    List<UserBadge> findByUser(User user);
}

