package com.game_store.model;

import jakarta.persistence.*;

@Entity
@Table(name = "userachievements")
public class UserAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserAchievementID")
    private int userAchievementID;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "AchievementID")
    private Achievement achievement;

    // Constructors
    public UserAchievement() {
    }

    public UserAchievement(User user, Achievement achievement) {
        this.user = user;
        this.achievement = achievement;
    }

    // Getters and Setters
    public int getUserAchievementID() {
        return userAchievementID;
    }

    public void setUserAchievementID(int userAchievementID) {
        this.userAchievementID = userAchievementID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }
}
