package com.game_store.controller;

import com.game_store.model.Game;
import com.game_store.model.Publisher;
import com.game_store.service.PublisherService;
import com.game_store.service.GameService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;
    private final PublisherService publisherService;

    @Autowired
    public GameController(GameService gameService, PublisherService publisherService) {
        this.gameService = gameService;
        this.publisherService = publisherService;
    }

    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        List<Game> games = gameService.getAllGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable int id) {
        Optional<Game> game = gameService.getGameById(id);
        return game.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Game> getGameByTitle(@PathVariable String title) {
        Optional<Game> game = gameService.getGameByTitle(title);
        return game.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<Game>> getGamesByGenre(@PathVariable String genre) {
        List<Game> games = gameService.getGamesByGenre(genre);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/price/{price}")
    public ResponseEntity<List<Game>> getGamesByPrice(@PathVariable BigDecimal price) {
        List<Game> games = gameService.getGamesByPrice(price);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/release-date/{releaseDate}")
    public ResponseEntity<List<Game>> getGamesByReleaseDate(@PathVariable LocalDate releaseDate) {
        List<Game> games = gameService.getGamesByReleaseDate(releaseDate);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @GetMapping("/publisher/{publisherId}")
    public ResponseEntity<List<Game>> getGamesByPublisherId(@PathVariable int publisherId) {
        Publisher publisherObj = publisherService.getPublisherById(publisherId).orElse(null);
        if (publisherObj == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Game> games = gameService.getGamesByPublisher(publisherObj);
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Game> saveGame(@Valid @RequestBody Game game) {
        Game savedGame = gameService.saveGame(game);
        return new ResponseEntity<>(savedGame, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGame(@PathVariable int id, @Valid @RequestBody Game updatedGame) {
        try {
            gameService.updateGame(id, updatedGame);
            return new ResponseEntity<>("Game updated successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGame(@PathVariable int id) {
        gameService.deleteGame(id);
        return new ResponseEntity<>("Game deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<Game> searchGames(@RequestParam String query) {
        return gameService.searchGames(query);
    }

}
