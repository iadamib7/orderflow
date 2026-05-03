package com.ibrahim.orderflow.dto;

import com.ibrahim.orderflow.model.Order;
import com.ibrahim.orderflow.model.OrderSide;
import com.ibrahim.orderflow.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;

// REPRESENTS AN ORDER RETURNED BY THE API
public class OrderResponse {

    private Long id;
    private String symbol;
    private OrderSide side;
    private BigDecimal price;
    private long quantity;
    private long remainingQuantity;
    private OrderStatus status;
    private Instant createdAt;

    // CONVERT AN ORDER ENTITY INTO AN API RESPONSE
    public static OrderResponse fromOrder(Order order) {

        // Create an empty response object.
        OrderResponse response = new OrderResponse();

        // Copy the order fields into the response.
        response.id = order.getId();
        response.symbol = order.getSymbol();
        response.side = order.getSide();
        response.price = order.getPrice();
        response.quantity = order.getQuantity();
        response.remainingQuantity = order.getRemainingQuantity();
        response.status = order.getStatus();
        response.createdAt = order.getCreatedAt();

        // Return the API-safe response.
        return response;
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
}