package com.game_store.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GameID")
    private int gameId;

    @Column(name = "Title", nullable = false)
    private String title;

    @Column(name = "Description")
    private String description;

    @Column(name = "Genre")
    private String genre;

    @Column(name = "Price")
    private BigDecimal price;

    @Column(name = "ReleaseDate")
    private LocalDate releaseDate;

    @ManyToOne
    @JoinColumn(name = "PublisherID")
    private Publisher publisher;

    // Constructors
    public Game() {
    }

    public Game(String title, String description, String genre, BigDecimal price, LocalDate releaseDate, Publisher publisher) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.price = price;
        this.releaseDate = releaseDate;
        this.publisher = publisher;
    }

    // Getters and Setters
    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    // toString method (for debugging/logging purposes)
    @Override
    public String toString() {
        return "Game{" +
                "gameId=" + gameId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", genre='" + genre + '\'' +
                ", price=" + price +
                ", releaseDate=" + releaseDate +
                ", publisher=" + publisher.toString() +
                '}';
    }
}
