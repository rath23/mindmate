package com.maq.mindmate.repository;

import java.util.UUID;

import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;

import com.maq.mindmate.models.User;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<User, UUID > {
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    User findByUserName(String username);


}
