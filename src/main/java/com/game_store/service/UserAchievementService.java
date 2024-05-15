package com.game_store.service;

import com.game_store.model.UserAchievement;
import com.game_store.repository.UserAchievementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAchievementService {

    private final UserAchievementRepository userAchievementRepository;

    @Autowired
    public UserAchievementService(UserAchievementRepository userAchievementRepository) {
        this.userAchievementRepository = userAchievementRepository;
    }

    public List<UserAchievement> getAllUserAchievements() {
        return userAchievementRepository.findAll();
    }

    public Optional<UserAchievement> getUserAchievementById(int id) {
        return userAchievementRepository.findById(id);
    }

    public List<UserAchievement> getUserAchievementsByUser(int userId) {
        return userAchievementRepository.findByUser_UserID(userId);
    }

    public List<UserAchievement> getUserAchievementsByAchievement(int achievementId) {
        return userAchievementRepository.findByAchievement_AchievementId(achievementId);
    }

    public UserAchievement saveUserAchievement(UserAchievement userAchievement) {
        return userAchievementRepository.save(userAchievement);
    }

    public UserAchievement updateUserAchievement(int id, UserAchievement updatedUserAchievement) {
        UserAchievement userAchievement = userAchievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Achievement not found with id: " + id));

        userAchievement.setUser(updatedUserAchievement.getUser());
        userAchievement.setAchievement(updatedUserAchievement.getAchievement());

        return userAchievementRepository.save(userAchievement);
    }

    public void deleteUserAchievement(int id) {
        userAchievementRepository.deleteById(id);
    }
}
