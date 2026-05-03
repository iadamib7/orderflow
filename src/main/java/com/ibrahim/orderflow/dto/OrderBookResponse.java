package com.ibrahim.orderflow.dto;

import com.ibrahim.orderflow.model.Order;

import java.util.List;

// REPRESENTS THE CURRENT BUY AND SELL SIDES OF AN ORDER BOOK
public class OrderBookResponse {

    private String symbol;
    private List<OrderResponse> buyOrders;
    private List<OrderResponse> sellOrders;

    // CREATE AN ORDER BOOK RESPONSE FROM BUY AND SELL ORDERS
    public OrderBookResponse(String symbol, List<Order> buyOrders, List<Order> sellOrders) {

        // Normalize the symbol for display.
        this.symbol = symbol.toUpperCase();

        // Convert buy order entities into API responses.
        this.buyOrders = buyOrders.stream()
                .map(OrderResponse::fromOrder)
                .toList();

        // Convert sell order entities into API responses.
        this.sellOrders = sellOrders.stream()
                .map(OrderResponse::fromOrder)
                .toList();
    }

    public String getSymbol() {
        return symbol;
    }

    public List<OrderResponse> getBuyOrders() {
        return buyOrders;
    }

    public List<OrderResponse> getSellOrders() {
        return sellOrders;
    }
}