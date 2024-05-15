package com.game_store.model;

import jakarta.persistence.*;

@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AchievementID")
    private int achievementId;

    @ManyToOne
    @JoinColumn(name = "GameID")
    private Game game;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description")
    private String description;

    // Constructors
    public Achievement() {
    }

    public Achievement(Game game, String name, String description) {
        this.game = game;
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public int getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(int achievementId) {
        this.achievementId = achievementId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // toString method (for debugging/logging purposes)
    @Override
    public String toString() {
        return "Achievement{" +
                "achievementId=" + achievementId +
                ", game=" + game.toString() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
