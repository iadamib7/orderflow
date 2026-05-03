package com.ibrahim.orderflow.service;

import com.ibrahim.orderflow.dto.CreateOrderRequest;
import com.ibrahim.orderflow.engine.MatchResult;
import com.ibrahim.orderflow.engine.MatchingEngine;
import com.ibrahim.orderflow.model.Order;
import com.ibrahim.orderflow.model.OrderStatus;
import com.ibrahim.orderflow.model.Trade;
import com.ibrahim.orderflow.repository.OrderRepository;
import com.ibrahim.orderflow.repository.TradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// HANDLES ORDER CREATION, LOOKUP, AND CANCELLATION
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final TradeRepository tradeRepository;
    private final MatchingEngine matchingEngine;

    // INJECT DATABASE REPOSITORIES AND CREATE MATCHING ENGINE
    public OrderService(OrderRepository orderRepository, TradeRepository tradeRepository) {

        // Store the order repository.
        this.orderRepository = orderRepository;

        // Store the trade repository.
        this.tradeRepository = tradeRepository;

        // Use one in-memory matching engine for now.
        this.matchingEngine = new MatchingEngine();
    }

    // CREATE AN ORDER AND MATCH IT AGAINST THE ORDER BOOK
    @Transactional
    public MatchResult createOrder(CreateOrderRequest request) {

        // Build the order entity from the request.
        Order order = new Order(
                request.getSymbol(),
                request.getSide(),
                request.getPrice(),
                request.getQuantity()
        );

        // Save first so the order has a database ID before matching.
        Order savedOrder = orderRepository.save(order);

        // Submit the saved order to the matching engine.
        MatchResult result = matchingEngine.submitOrder(savedOrder);

        // Save the updated incoming order after any fills.
        orderRepository.save(result.getIncomingOrder());

        // Save every trade created by the matching engine.
        for (Trade trade : result.getTrades()) {
            tradeRepository.save(trade);
        }

        // Return the matching result.
        return result;
    }

    // GET ONE ORDER BY ID
    public Order getOrderById(Long id) {

        // Return the order or throw a simple error for now.
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
    }

    // GET ORDERS FOR A SYMBOL
    public List<Order> getOrdersBySymbol(String symbol) {

        // Normalize symbol before lookup.
        return orderRepository.findBySymbolOrderByCreatedAtDesc(symbol.toUpperCase());
    }

    // CANCEL AN ORDER
    @Transactional
    public Order cancelOrder(Long id) {

        // Load the order.
        Order order = getOrderById(id);

        // Do not cancel orders that are already filled.
        if (order.getStatus() == OrderStatus.FILLED) {
            throw new IllegalArgumentException("Cannot cancel a filled order.");
        }

        // Mark the order as cancelled.
        order.cancel();

        // Save and return the cancelled order.
        return orderRepository.save(order);
    }

    // EXPOSE THE MATCHING ENGINE FOR ORDER BOOK SNAPSHOTS LATER
    public MatchingEngine getMatchingEngine() {
        return matchingEngine;
    }
}