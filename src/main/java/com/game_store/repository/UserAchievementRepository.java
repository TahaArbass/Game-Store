package com.game_store.repository;

import com.game_store.model.UserAchievement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAchievementRepository extends JpaRepository<UserAchievement, Integer> {
    List<UserAchievement> findByUser_UserID(int userID);
    List<UserAchievement> findByAchievement_AchievementId(int achievementId);
}
