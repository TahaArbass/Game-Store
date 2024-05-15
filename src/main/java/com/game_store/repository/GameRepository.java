package com.game_store.repository;

import com.game_store.model.Game;
import com.game_store.model.Publisher;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findByTitle(String title);
    List<Game> findByGenre(String genre);
    List<Game> findByPrice(BigDecimal price);
    List<Game> findByReleaseDate(LocalDate releaseDate);
    List<Game> findByPublisher(Publisher publisher);
    List<Game> findByTitleContainingIgnoreCase(String title);

}
