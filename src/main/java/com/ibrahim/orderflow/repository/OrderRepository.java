package com.ibrahim.orderflow.repository;

import com.ibrahim.orderflow.model.Order;
import com.ibrahim.orderflow.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// PROVIDES DATABASE ACCESS FOR ORDERS
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Find all orders for one symbol.
    List<Order> findBySymbolOrderByCreatedAtDesc(String symbol);

    // Find all orders with a specific status.
    List<Order> findByStatus(OrderStatus status);

    // Find active orders for one symbol.
    List<Order> findBySymbolAndStatusInOrderByCreatedAtAsc(String symbol, List<OrderStatus> statuses);
}