package com.game_store.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PurchaseID")
    private int purchaseId;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "GameID")
    private Game game;

    @Column(name = "PurchaseDate")
    private LocalDateTime purchaseDate;

    @Column(name = "PricePaid")
    private BigDecimal pricePaid;

    // Constructors
    public Purchase() {
    }

    public Purchase(User user, Game game, LocalDateTime purchaseDate, BigDecimal pricePaid) {
        this.user = user;
        this.game = game;
        this.purchaseDate = purchaseDate;
        this.pricePaid = pricePaid;
    }

    // Getters and Setters
    public int getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public BigDecimal getPricePaid() {
        return pricePaid;
    }

    public void setPricePaid(BigDecimal pricePaid) {
        this.pricePaid = pricePaid;
    }

    // toString method (for debugging/logging purposes)
    @Override
    public String toString() {
        return "Purchase{" +
                "purchaseId=" + purchaseId +
                ", user=" + user.toString() +
                ", game=" + game.toString() +
                ", purchaseDate=" + purchaseDate +
                ", pricePaid=" + pricePaid +
                '}';
    }
}
