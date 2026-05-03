package com.ibrahim.orderflow.repository;

import com.ibrahim.orderflow.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// PROVIDES DATABASE ACCESS FOR TRADES
public interface TradeRepository extends JpaRepository<Trade, Long> {

    // Find all trades for one symbol, newest first.
    List<Trade> findBySymbolOrderByExecutedAtDesc(String symbol);
}