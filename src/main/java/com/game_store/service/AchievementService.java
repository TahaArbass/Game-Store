package com.game_store.service;

import com.game_store.model.Achievement;
import com.game_store.model.Game;
import com.game_store.repository.AchievementRepository;
import com.game_store.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final GameRepository gameRepository;

    @Autowired
    public AchievementService(AchievementRepository achievementRepository, GameRepository gameRepository) {
        this.achievementRepository = achievementRepository;
        this.gameRepository = gameRepository;
    }

    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    public Optional<Achievement> getAchievementById(int id) {
        return achievementRepository.findById(id);
    }

    public List<Achievement> getAchievementsByGame(int gameId) {
        // get the game by id then get achievements
        return achievementRepository.findByGame(gameRepository.findById(gameId)
        .orElseThrow(() -> new RuntimeException("Game not found with id: " + gameId)));
    }

    public List<Achievement> getAchievementsByName(String name) {
        return achievementRepository.findByName(name);
    }

    public Achievement saveAchievement(Achievement achievement) {
        return achievementRepository.save(achievement);
    }

    public Achievement updateAchievement(int id, Achievement updatedAchievement) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found with id: " + id));

        achievement.setName(updatedAchievement.getName());
        achievement.setDescription(updatedAchievement.getDescription());
        achievement.setGame(updatedAchievement.getGame());

        return achievementRepository.save(achievement);
    }

    public void deleteAchievement(int id) {
        achievementRepository.deleteById(id);
    }
}
