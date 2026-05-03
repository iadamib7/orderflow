package com.ibrahim.orderflow.engine;

import com.ibrahim.orderflow.model.Order;
import com.ibrahim.orderflow.model.OrderSide;
import com.ibrahim.orderflow.model.Trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// MATCHES BUY AND SELL ORDERS USING PRICE-TIME PRIORITY
public class MatchingEngine {

    // Store one order book per trading symbol.
    private final Map<String, OrderBookService> orderBooksBySymbol;

    // CREATE AN EMPTY MATCHING ENGINE
    public MatchingEngine() {

        // Initialize the symbol-to-order-book map.
        this.orderBooksBySymbol = new HashMap<>();
    }

    // SUBMIT ONE ORDER AND RETURN THE TRADES IT CREATED
    public synchronized MatchResult submitOrder(Order incomingOrder) {

        // Normalize the symbol before looking up the book.
        String symbol = incomingOrder.getSymbol().toUpperCase();

        // Create the symbol's order book if this is the first order for that symbol.
        OrderBookService orderBook = orderBooksBySymbol.computeIfAbsent(symbol, ignored -> new OrderBookService());

        // Store trades created during this submission.
        List<Trade> trades = new ArrayList<>();

        // Match buy orders against resting sell orders.
        if (incomingOrder.getSide() == OrderSide.BUY) {
            matchBuyOrder(incomingOrder, orderBook, trades);
        }

        // Match sell orders against resting buy orders.
        if (incomingOrder.getSide() == OrderSide.SELL) {
            matchSellOrder(incomingOrder, orderBook, trades);
        }

        // If the incoming order still has quantity left, add it to the book.
        if (incomingOrder.isActive()) {
            orderBook.addOrder(incomingOrder);
        }

        // Return the updated order and the trades created.
        return new MatchResult(incomingOrder, trades);
    }

    // MATCH AN INCOMING BUY ORDER AGAINST RESTING SELL ORDERS
    private void matchBuyOrder(Order buyOrder, OrderBookService orderBook, List<Trade> trades) {

        // Keep matching while the buy order still has quantity.
        while (buyOrder.isActive()) {

            // Look at the best available sell order.
            Order bestSell = orderBook.peekBestSell();

            // Stop if there are no sell orders available.
            if (bestSell == null) {
                return;
            }

            // Stop if the best sell price is too expensive for this buy order.
            if (!pricesCross(buyOrder.getPrice(), bestSell.getPrice(), OrderSide.BUY)) {
                return;
            }

            // Execute a trade between the incoming buy and resting sell.
            Trade trade = executeTrade(buyOrder, bestSell, bestSell.getPrice());

            // Store the generated trade.
            trades.add(trade);

            // Remove the resting sell if it has been fully filled.
            if (!bestSell.isActive()) {
                orderBook.removeBestSell();
            }
        }
    }

    // MATCH AN INCOMING SELL ORDER AGAINST RESTING BUY ORDERS
    private void matchSellOrder(Order sellOrder, OrderBookService orderBook, List<Trade> trades) {

        // Keep matching while the sell order still has quantity.
        while (sellOrder.isActive()) {

            // Look at the best available buy order.
            Order bestBuy = orderBook.peekBestBuy();

            // Stop if there are no buy orders available.
            if (bestBuy == null) {
                return;
            }

            // Stop if the best buy price is too low for this sell order.
            if (!pricesCross(sellOrder.getPrice(), bestBuy.getPrice(), OrderSide.SELL)) {
                return;
            }

            // Execute a trade between the resting buy and incoming sell.
            Trade trade = executeTrade(bestBuy, sellOrder, bestBuy.getPrice());

            // Store the generated trade.
            trades.add(trade);

            // Remove the resting buy if it has been fully filled.
            if (!bestBuy.isActive()) {
                orderBook.removeBestBuy();
            }
        }
    }

    // CHECK WHETHER TWO PRICES CAN MATCH
    private boolean pricesCross(BigDecimal incomingPrice, BigDecimal restingPrice, OrderSide incomingSide) {

        // A buy crosses when its price is greater than or equal to the sell price.
        if (incomingSide == OrderSide.BUY) {
            return incomingPrice.compareTo(restingPrice) >= 0;
        }

        // A sell crosses when its price is less than or equal to the buy price.
        return incomingPrice.compareTo(restingPrice) <= 0;
    }

    // EXECUTE ONE TRADE BETWEEN A BUY ORDER AND A SELL ORDER
    private Trade executeTrade(Order buyOrder, Order sellOrder, BigDecimal executionPrice) {

        // The trade quantity is limited by the smaller remaining quantity.
        long executedQuantity = Math.min(
                buyOrder.getRemainingQuantity(),
                sellOrder.getRemainingQuantity()
        );

        // Reduce the remaining quantity on the buy order.
        buyOrder.fill(executedQuantity);

        // Reduce the remaining quantity on the sell order.
        sellOrder.fill(executedQuantity);

        // Create the trade record.
        return new Trade(
                buyOrder.getSymbol(),
                buyOrder.getId(),
                sellOrder.getId(),
                executionPrice,
                executedQuantity
        );
    }

    // GET AN ORDER BOOK FOR A SYMBOL
    public OrderBookService getOrderBook(String symbol) {

        // Return the existing book, or a new empty book if the symbol has no orders yet.
        return orderBooksBySymbol.computeIfAbsent(symbol.toUpperCase(), ignored -> new OrderBookService());
    }
}