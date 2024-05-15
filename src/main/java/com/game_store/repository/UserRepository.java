package com.game_store.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.game_store.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
