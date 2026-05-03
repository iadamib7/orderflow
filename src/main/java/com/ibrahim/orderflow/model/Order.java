package com.ibrahim.orderflow.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

// STORE EACH BUY OR SELL ORDER IN THE DATABASE
@Entity
@Table(name = "orders")
public class Order {

    // Use the database to generate a unique order ID.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Store the traded symbol, such as AAPL or BTCUSD.
    @Column(nullable = false)
    private String symbol;

    // Store whether this is a buy or sell order.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderSide side;

    // Store the limit price for the order.
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal price;

    // Store the original requested quantity.
    @Column(nullable = false)
    private long quantity;

    // Store how much quantity is still unmatched.
    @Column(nullable = false)
    private long remainingQuantity;

    // Store the order's current lifecycle state.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    // Store when the order was created for time-priority matching.
    @Column(nullable = false)
    private Instant createdAt;

    // REQUIRED BY JPA
    protected Order() {
    }

    // CREATE A NEW ORDER WITH DEFAULT OPEN STATUS
    public Order(String symbol, OrderSide side, BigDecimal price, long quantity) {

        // Normalize the symbol so AAPL and aapl are treated the same.
        this.symbol = symbol.toUpperCase();

        // Store whether this order buys or sells.
        this.side = side;

        // Store the order's limit price.
        this.price = price;

        // Store the original quantity.
        this.quantity = quantity;

        // At creation, the full quantity is still unmatched.
        this.remainingQuantity = quantity;

        // New orders start as open.
        this.status = OrderStatus.OPEN;

        // Timestamp supports price-time-priority matching.
        this.createdAt = Instant.now();
    }

    // UPDATE STATUS BASED ON REMAINING QUANTITY
    public void refreshStatusAfterFill() {

        // If nothing remains, the order is fully filled.
        if (this.remainingQuantity == 0) {
            this.status = OrderStatus.FILLED;
            return;
        }

        // If some but not all quantity remains, the order is partially filled.
        if (this.remainingQuantity < this.quantity) {
            this.status = OrderStatus.PARTIALLY_FILLED;
            return;
        }

        // Otherwise, the order has not been filled yet.
        this.status = OrderStatus.OPEN;
    }

    // REDUCE REMAINING QUANTITY AFTER A TRADE
    public void fill(long executedQuantity) {

        // Prevent filling more than what remains.
        if (executedQuantity > this.remainingQuantity) {
            throw new IllegalArgumentException("Executed quantity cannot exceed remaining quantity.");
        }

        // Subtract the matched quantity.
        this.remainingQuantity -= executedQuantity;

        // Update the order status after the fill.
        refreshStatusAfterFill();
    }

    // CANCEL AN ORDER IF IT IS STILL ACTIVE
    public void cancel() {

        // Mark the order as cancelled.
        this.status = OrderStatus.CANCELLED;
    }

    // CHECK WHETHER THE ORDER CAN STILL BE MATCHED
    public boolean isActive() {

        // Only open and partially filled orders should remain matchable.
        return this.status == OrderStatus.OPEN || this.status == OrderStatus.PARTIALLY_FILLED;
    }

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public OrderSide getSide() {
        return side;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }

    public long getRemainingQuantity() {
        return remainingQuantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }
}