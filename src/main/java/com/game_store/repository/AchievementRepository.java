package com.game_store.repository;

import com.game_store.model.Achievement;
import com.game_store.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, Integer> {

    List<Achievement> findByGame(Game game);

    List<Achievement> findByName(String name);
}
