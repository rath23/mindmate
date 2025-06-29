package com.maq.mindmate.repository;

import com.maq.mindmate.models.BlockedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {
    Optional<BlockedUser> findByNicknameAndRoom(String nickname, String room);
    List<BlockedUser> findByBlockedUntilAfter(LocalDateTime now);
}
