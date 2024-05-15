package com.game_store.controller;

import com.game_store.model.UserAchievement;
import com.game_store.service.UserAchievementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/userachievements")
public class UserAchievementController {

    private final UserAchievementService userAchievementService;

    @Autowired
    public UserAchievementController(UserAchievementService userAchievementService) {
        this.userAchievementService = userAchievementService;
    }

    @GetMapping
    public ResponseEntity<List<UserAchievement>> getAllUserAchievements() {
        List<UserAchievement> userAchievements = userAchievementService.getAllUserAchievements();
        return ResponseEntity.ok(userAchievements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAchievement> getUserAchievementById(@PathVariable("id") int id) {
        Optional<UserAchievement> userAchievement = userAchievementService.getUserAchievementById(id);
        return userAchievement.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAchievement>> getUserAchievementsByUser(@PathVariable("userId") int userId) {
        List<UserAchievement> userAchievements = userAchievementService.getUserAchievementsByUser(userId);
        return ResponseEntity.ok(userAchievements);
    }

    @GetMapping("/achievement/{achievementId}")
    public ResponseEntity<List<UserAchievement>> getUserAchievementsByAchievement(
            @PathVariable("achievementId") int achievementId) {
        List<UserAchievement> userAchievements = userAchievementService.getUserAchievementsByAchievement(achievementId);
        return ResponseEntity.ok(userAchievements);
    }

    @PostMapping
    public ResponseEntity<UserAchievement> saveUserAchievement(@RequestBody UserAchievement userAchievement) {
        UserAchievement savedUserAchievement = userAchievementService.saveUserAchievement(userAchievement);
        return new ResponseEntity<>(savedUserAchievement, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAchievement> updateUserAchievement(@PathVariable("id") int id,
            @RequestBody UserAchievement userAchievement) {
        UserAchievement updatedUserAchievement = userAchievementService.updateUserAchievement(id, userAchievement);
        return ResponseEntity.ok(updatedUserAchievement);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAchievement(@PathVariable("id") int id) {
        userAchievementService.deleteUserAchievement(id);
        return ResponseEntity.noContent().build();
    }
}
