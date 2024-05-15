package com.game_store.controller;

import com.game_store.model.Purchase;
import com.game_store.model.User;
import com.game_store.model.Game;

import com.game_store.service.PurchaseService;
import com.game_store.service.UserService;
import com.game_store.service.GameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, UserService userService, GameService gameService) {
        this.purchaseService = purchaseService;
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases() {
        List<Purchase> purchases = purchaseService.getAllPurchases();
        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable int id) {
        Optional<Purchase> purchase = purchaseService.getPurchaseById(id);
        return purchase.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Purchase>> getPurchasesByUserId(@PathVariable int userId) {
        // find user by id
        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Purchase> purchases = purchaseService.getPurchasesByUser(user);
        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }

    @GetMapping("/game/{gameId}")
    public ResponseEntity<List<Purchase>> getPurchasesByGameId(@PathVariable int gameId) {
        // find game by id
        Game game = gameService.getGameById(gameId).orElse(null);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Purchase> purchases = purchaseService.getPurchasesByGame(game);
        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/game/{gameId}")
    public ResponseEntity<List<Purchase>> getPurchasesByUserIdAndGameId(@PathVariable int userId, @PathVariable int gameId) {
        User user = userService.getUserById(userId).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Game game = gameService.getGameById(gameId).orElse(null);
        if (game == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        List<Purchase> purchases = purchaseService.getPurchasesByUserAndGame(user, game);
        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }

    @GetMapping("/purchase-date/{purchaseDate}")
    public ResponseEntity<List<Purchase>> getPurchasesByPurchaseDate(@PathVariable LocalDate purchaseDate) {
        List<Purchase> purchases = purchaseService.getPurchasesByPurchaseDate(purchaseDate);
        if(purchases.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }

    @GetMapping("/price-paid/{pricePaid}")
    public ResponseEntity<List<Purchase>> getPurchasesByPricePaid(@PathVariable BigDecimal pricePaid) {
        List<Purchase> purchases = purchaseService.getPurchasesByPricePaid(pricePaid);
        if(purchases.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(purchases, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Purchase> savePurchase(@Valid @RequestBody Purchase purchase) {
        Purchase savedPurchase = purchaseService.savePurchase(purchase);
        return new ResponseEntity<>(savedPurchase, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePurchase(@PathVariable int id,@Valid @RequestBody Purchase updatedPurchase) {
        try {
            if (purchaseService.getPurchaseById(id).isEmpty()) {
               return new ResponseEntity<>("Purchase not found", HttpStatus.NOT_FOUND); 
            }
            purchaseService.updatePurchase(id, updatedPurchase);
            return new ResponseEntity<>("Purchase updated successfully", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePurchase(@PathVariable int id) {
        Purchase purchase = purchaseService.getPurchaseById(id).orElse(null);
        if (purchase == null) {
            return new ResponseEntity<>("Purchase not found", HttpStatus.NOT_FOUND);
        }
        purchaseService.deletePurchase(id);
        return new ResponseEntity<>("Purchase deleted successfully", HttpStatus.OK);
    }
}
