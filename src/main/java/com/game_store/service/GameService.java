package com.game_store.service;

import com.game_store.model.Game;
import com.game_store.model.Publisher;
import com.game_store.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public Optional<Game> getGameById(int id) {
        return gameRepository.findById(id);
    }

    public Optional<Game> getGameByTitle(String title) {
        return gameRepository.findByTitle(title);
    }

    public List<Game> getGamesByGenre(String genre) {
        return gameRepository.findByGenre(genre);
    }

    public List<Game> getGamesByPrice(BigDecimal price) {
        return gameRepository.findByPrice(price);
    }

    public List<Game> getGamesByReleaseDate(LocalDate releaseDate) {
        return gameRepository.findByReleaseDate(releaseDate);
    }

    public List<Game> getGamesByPublisher(Publisher publisher) {
        return gameRepository.findByPublisher(publisher);
    }

    public Game saveGame(Game game) {
        return gameRepository.save(game);
    }

    public void updateGame(int id, Game updatedGame) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            game.setTitle(updatedGame.getTitle());
            game.setDescription(updatedGame.getDescription());
            game.setGenre(updatedGame.getGenre());
            game.setPrice(updatedGame.getPrice());
            game.setReleaseDate(updatedGame.getReleaseDate());
            game.setPublisher(updatedGame.getPublisher());
            gameRepository.save(game);
        } else {
            // Handle the case when the game does not exist
            throw new RuntimeException("Game not found with id: " + id);
        }
    }

    public void deleteGame(int id) {
        gameRepository.deleteById(id);
    }

    public List<Game> searchGames(String query) {
        return gameRepository.findByTitleContainingIgnoreCase(query);
    }
}
