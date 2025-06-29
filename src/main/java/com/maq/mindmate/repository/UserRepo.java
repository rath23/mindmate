package com.maq.mindmate.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maq.mindmate.models.User;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<User, UUID > {
    User findByUserName(String username);
}
