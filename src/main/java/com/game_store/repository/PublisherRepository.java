package com.game_store.repository;

import com.game_store.model.Publisher;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    Optional<Publisher> findByUsername(String username);

    Optional<Publisher> findByEmail(String email);

    Optional<Publisher> findByUsernameAndPassword(String username, String password);
}
