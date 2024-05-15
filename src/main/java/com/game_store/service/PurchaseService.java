package com.game_store.service;

import com.game_store.model.Game;
import com.game_store.model.Purchase;
import com.game_store.model.User;
import com.game_store.repository.PurchaseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;


    @Autowired
    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;        
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Optional<Purchase> getPurchaseById(int id) {
        return purchaseRepository.findById(id);
    }

    public List<Purchase> getPurchasesByUser(User user) {

        return purchaseRepository.findByUser(user);
    }

    public List<Purchase> getPurchasesByGame(Game game) {
        return purchaseRepository.findByGame(game);
    }

    public List<Purchase> getPurchasesByUserAndGame(User user, Game game) {
        return purchaseRepository.findByUserAndGame(user, game);
    }

    public List<Purchase> getPurchasesByPurchaseDate(LocalDate purchaseDate) {
        return purchaseRepository.findAllByPurchaseDate(purchaseDate);
    }

    public List<Purchase> getPurchasesByPricePaid(BigDecimal pricePaid) {
        return purchaseRepository.findByPricePaid(pricePaid);
    }

    public Purchase savePurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public void updatePurchase(int id, Purchase updatedPurchase) {
        Optional<Purchase> optionalPurchase = purchaseRepository.findById(id);
        if (optionalPurchase.isPresent()) {
            Purchase purchase = optionalPurchase.get();
            purchase.setUser(updatedPurchase.getUser());
            purchase.setGame(updatedPurchase.getGame());
            purchase.setPurchaseDate(updatedPurchase.getPurchaseDate());
            purchase.setPricePaid(updatedPurchase.getPricePaid());
            purchaseRepository.save(purchase);
        } else {
            // Handle the case when the purchase does not exist
            throw new RuntimeException("Purchase not found with id: " + id);
        }
    }

    public void deletePurchase(int id) {
        purchaseRepository.deleteById(id);
    }
}
