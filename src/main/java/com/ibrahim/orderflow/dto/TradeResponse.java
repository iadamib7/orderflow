package com.ibrahim.orderflow.dto;

import com.ibrahim.orderflow.model.Trade;

import java.math.BigDecimal;
import java.time.Instant;

// REPRESENTS A TRADE RETURNED BY THE API
public class TradeResponse {

    private Long id;
    private String symbol;
    private Long buyOrderId;
    private Long sellOrderId;
    private BigDecimal price;
    private long quantity;
    private Instant executedAt;

    // CONVERT A TRADE ENTITY INTO AN API RESPONSE
    public static TradeResponse fromTrade(Trade trade) {

        // Create an empty response object.
        TradeResponse response = new TradeResponse();

        // Copy the trade fields into the response.
        response.id = trade.getId();
        response.symbol = trade.getSymbol();
        response.buyOrderId = trade.getBuyOrderId();
        response.sellOrderId = trade.getSellOrderId();
        response.price = trade.getPrice();
        response.quantity = trade.getQuantity();
        response.executedAt = trade.getExecutedAt();

        // Return the API-safe response.
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public Long getBuyOrderId() {
        return buyOrderId;
    }

    public Long getSellOrderId() {
        return sellOrderId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public Instant getExecutedAt() {
        return executedAt;
    }
}