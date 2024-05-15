package com.game_store.repository;

import com.game_store.model.Game;
import com.game_store.model.Purchase;
import com.game_store.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {

    List<Purchase> findByUser(User user);

    List <Purchase> findByGame(Game game);

    List <Purchase> findByUserAndGame(User user, Game game);

    List<Purchase> findAllByPurchaseDate(LocalDate purchaseDate);

    List <Purchase> findByPricePaid(BigDecimal pricePaid);

}
