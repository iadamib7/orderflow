package com.ibrahim.orderflow.engine;

import com.ibrahim.orderflow.model.Order;
import com.ibrahim.orderflow.model.OrderSide;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

// STORES BUY AND SELL ORDERS FOR ONE SYMBOL
public class OrderBookService {

    // BUY ORDERS: HIGHEST PRICE FIRST, THEN EARLIEST CREATED TIME
    private final PriorityQueue<Order> buyOrders;

    // SELL ORDERS: LOWEST PRICE FIRST, THEN EARLIEST CREATED TIME
    private final PriorityQueue<Order> sellOrders;

    // CREATE AN EMPTY ORDER BOOK
    public OrderBookService() {

        // Buy orders should prioritize the highest price first.
        this.buyOrders = new PriorityQueue<>(
                Comparator.comparing(Order::getPrice).reversed()
                        .thenComparing(Order::getCreatedAt)
        );

        // Sell orders should prioritize the lowest price first.
        this.sellOrders = new PriorityQueue<>(
                Comparator.comparing(Order::getPrice)
                        .thenComparing(Order::getCreatedAt)
        );
    }

    // ADD AN ORDER TO THE CORRECT SIDE OF THE BOOK
    public void addOrder(Order order) {

        // Buy orders go into the buy queue.
        if (order.getSide() == OrderSide.BUY) {
            buyOrders.add(order);
            return;
        }

        // Sell orders go into the sell queue.
        sellOrders.add(order);
    }

    // GET THE BEST BUY ORDER WITHOUT REMOVING IT
    public Order peekBestBuy() {

        // Return the highest-priority buy order.
        return buyOrders.peek();
    }

    // GET THE BEST SELL ORDER WITHOUT REMOVING IT
    public Order peekBestSell() {

        // Return the highest-priority sell order.
        return sellOrders.peek();
    }

    // REMOVE THE BEST BUY ORDER
    public Order removeBestBuy() {

        // Remove and return the best buy order.
        return buyOrders.poll();
    }

    // REMOVE THE BEST SELL ORDER
    public Order removeBestSell() {

        // Remove and return the best sell order.
        return sellOrders.poll();
    }

    // GET A SNAPSHOT OF BUY ORDERS FOR DISPLAY OR TESTING
    public List<Order> getBuyOrdersSnapshot() {

        // Copy the priority queue into a list.
        List<Order> snapshot = new ArrayList<>(buyOrders);

        // Sort the copy using the same priority rule.
        snapshot.sort(
                Comparator.comparing(Order::getPrice).reversed()
                        .thenComparing(Order::getCreatedAt)
        );

        // Return the sorted snapshot.
        return snapshot;
    }

    // GET A SNAPSHOT OF SELL ORDERS FOR DISPLAY OR TESTING
    public List<Order> getSellOrdersSnapshot() {

        // Copy the priority queue into a list.
        List<Order> snapshot = new ArrayList<>(sellOrders);

        // Sort the copy using the same priority rule.
        snapshot.sort(
                Comparator.comparing(Order::getPrice)
                        .thenComparing(Order::getCreatedAt)
        );

        // Return the sorted snapshot.
        return snapshot;
    }
}