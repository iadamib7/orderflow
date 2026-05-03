package com.ibrahim.orderflow.service;

import com.ibrahim.orderflow.model.Trade;
import com.ibrahim.orderflow.repository.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

// HANDLES TRADE LOOKUP LOGIC
@Service
public class TradeService {

    private final TradeRepository tradeRepository;

    // INJECT TRADE REPOSITORY
    public TradeService(TradeRepository tradeRepository) {

        // Store the trade repository.
        this.tradeRepository = tradeRepository;
    }

    // GET ALL TRADES FOR A SYMBOL
    public List<Trade> getTradesBySymbol(String symbol) {

        // Normalize symbol before lookup.
        return tradeRepository.findBySymbolOrderByExecutedAtDesc(symbol.toUpperCase());
    }
}