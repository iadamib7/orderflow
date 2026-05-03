package com.ibrahim.orderflow.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

// STORE EACH COMPLETED MATCH BETWEEN A BUY ORDER AND A SELL ORDER
@Entity
@Table(name = "trades")
public class Trade {

    // Use the database to generate a unique trade ID.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Store the traded symbol, such as AAPL or BTCUSD.
    @Column(nullable = false)
    private String symbol;

    // Store the buy order involved in this trade.
    @Column(nullable = false)
    private Long buyOrderId;

    // Store the sell order involved in this trade.
    @Column(nullable = false)
    private Long sellOrderId;

    // Store the execution price.
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    // Store the executed quantity.
    @Column(nullable = false)
    private long quantity;

    // Store when the trade happened.
    @Column(nullable = false)
    private Instant executedAt;

    // REQUIRED BY JPA
    protected Trade() {
    }

    // CREATE A NEW TRADE RECORD
    public Trade(String symbol, Long buyOrderId, Long sellOrderId, BigDecimal price, long quantity) {

        // Normalize the symbol for consistent lookup.
        this.symbol = symbol.toUpperCase();

        // Store the matched buy order ID.
        this.buyOrderId = buyOrderId;

        // Store the matched sell order ID.
        this.sellOrderId = sellOrderId;

        // Store the execution price.
        this.price = price;

        // Store the executed quantity.
        this.quantity = quantity;

        // Timestamp when the trade was created.
        this.executedAt = Instant.now();
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

    public void setId(Long id) {
        this.id = id;
    }
}