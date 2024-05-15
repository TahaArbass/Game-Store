package com.game_store.controller;

import com.game_store.model.Achievement;
import com.game_store.service.AchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    public ResponseEntity<List<Achievement>> getAllAchievements() {
        List<Achievement> achievements = achievementService.getAllAchievements();
        return new ResponseEntity<>(achievements, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Achievement> getAchievementById(@PathVariable int id) {
        Optional<Achievement> achievement = achievementService.getAchievementById(id);
        return achievement.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<Achievement>> getAchievementsByGame(@PathVariable int gameId) {
        // get the achievements by game id
        List<Achievement> achievements = achievementService.getAchievementsByGame(gameId);
        return new ResponseEntity<>(achievements, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Achievement>> getAchievementsByName(@PathVariable String name) {
        List<Achievement> achievements = achievementService.getAchievementsByName(name);
        return new ResponseEntity<>(achievements, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Achievement> saveAchievement(@RequestBody Achievement achievement) {
        Achievement savedAchievement = achievementService.saveAchievement(achievement);
        return new ResponseEntity<>(savedAchievement, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Achievement> updateAchievement(@PathVariable int id, @RequestBody Achievement updatedAchievement) {
        Achievement achievement = achievementService.updateAchievement(id, updatedAchievement);
        return new ResponseEntity<>(achievement, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable int id) {
        achievementService.deleteAchievement(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
