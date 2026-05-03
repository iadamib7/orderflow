package com.ibrahim.orderflow.engine;

import com.ibrahim.orderflow.model.Order;
import com.ibrahim.orderflow.model.OrderSide;
import com.ibrahim.orderflow.model.OrderStatus;
import com.ibrahim.orderflow.model.Trade;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

// TEST CORE MATCHING ENGINE BEHAVIOR
class MatchingEngineTest {

    // TEST THAT AN UNMATCHED BUY ORDER RESTS IN THE BOOK
    @Test
    void buyOrderRestsWhenNoSellOrderExists() {

        // Create a fresh matching engine.
        MatchingEngine engine = new MatchingEngine();

        // Create a buy order for AAPL.
        Order buyOrder = new Order("AAPL", OrderSide.BUY, new BigDecimal("100.00"), 10);
        buyOrder.setId(1L);

        // Submit the order.
        MatchResult result = engine.submitOrder(buyOrder);

        // Confirm no trades happened.
        assertTrue(result.getTrades().isEmpty());

        // Confirm the order remains open.
        assertEquals(OrderStatus.OPEN, result.getIncomingOrder().getStatus());

        // Confirm the buy order is now resting in the order book.
        assertEquals(1, engine.getOrderBook("AAPL").getBuyOrdersSnapshot().size());
    }

    // TEST THAT A BUY ORDER MATCHES A CHEAPER SELL ORDER
    @Test
    void buyOrderMatchesSellOrderWhenBuyPriceIsHighEnough() {

        // Create a fresh matching engine.
        MatchingEngine engine = new MatchingEngine();

        // Create a resting sell order.
        Order sellOrder = new Order("AAPL", OrderSide.SELL, new BigDecimal("95.00"), 10);
        sellOrder.setId(1L);

        // Submit the sell order so it rests in the book.
        engine.submitOrder(sellOrder);

        // Create a buy order willing to pay more than the sell price.
        Order buyOrder = new Order("AAPL", OrderSide.BUY, new BigDecimal("100.00"), 10);
        buyOrder.setId(2L);

        // Submit the buy order.
        MatchResult result = engine.submitOrder(buyOrder);

        // Confirm exactly one trade happened.
        assertEquals(1, result.getTrades().size());

        // Read the generated trade.
        Trade trade = result.getTrades().get(0);

        // Confirm the trade used the resting sell price.
        assertEquals(new BigDecimal("95.00"), trade.getPrice());

        // Confirm the full quantity traded.
        assertEquals(10, trade.getQuantity());

        // Confirm both orders are filled.
        assertEquals(OrderStatus.FILLED, buyOrder.getStatus());
        assertEquals(OrderStatus.FILLED, sellOrder.getStatus());

        // Confirm the order book is empty after the full match.
        assertTrue(engine.getOrderBook("AAPL").getBuyOrdersSnapshot().isEmpty());
        assertTrue(engine.getOrderBook("AAPL").getSellOrdersSnapshot().isEmpty());
    }

    // TEST PARTIAL FILL WHEN INCOMING ORDER IS SMALLER
    @Test
    void incomingOrderCanPartiallyFillRestingOrder() {

        // Create a fresh matching engine.
        MatchingEngine engine = new MatchingEngine();

        // Create a larger resting sell order.
        Order sellOrder = new Order("AAPL", OrderSide.SELL, new BigDecimal("95.00"), 20);
        sellOrder.setId(1L);

        // Submit the sell order.
        engine.submitOrder(sellOrder);

        // Create a smaller incoming buy order.
        Order buyOrder = new Order("AAPL", OrderSide.BUY, new BigDecimal("100.00"), 5);
        buyOrder.setId(2L);

        // Submit the buy order.
        MatchResult result = engine.submitOrder(buyOrder);

        // Confirm exactly one trade happened.
        assertEquals(1, result.getTrades().size());

        // Confirm the incoming buy order was fully filled.
        assertEquals(OrderStatus.FILLED, buyOrder.getStatus());

        // Confirm the resting sell order still has quantity left.
        assertEquals(OrderStatus.PARTIALLY_FILLED, sellOrder.getStatus());
        assertEquals(15, sellOrder.getRemainingQuantity());

        // Confirm the partially filled sell order still rests in the book.
        assertEquals(1, engine.getOrderBook("AAPL").getSellOrdersSnapshot().size());
    }

    // TEST PRICE-TIME PRIORITY BETWEEN TWO SELL ORDERS AT DIFFERENT PRICES
    @Test
    void matchingUsesBestPriceFirst() {

        // Create a fresh matching engine.
        MatchingEngine engine = new MatchingEngine();

        // Create a more expensive sell order.
        Order expensiveSell = new Order("AAPL", OrderSide.SELL, new BigDecimal("101.00"), 10);
        expensiveSell.setId(1L);

        // Create a cheaper sell order.
        Order cheaperSell = new Order("AAPL", OrderSide.SELL, new BigDecimal("99.00"), 10);
        cheaperSell.setId(2L);

        // Submit both sell orders.
        engine.submitOrder(expensiveSell);
        engine.submitOrder(cheaperSell);

        // Create a buy order that can match both prices.
        Order buyOrder = new Order("AAPL", OrderSide.BUY, new BigDecimal("105.00"), 10);
        buyOrder.setId(3L);

        // Submit the buy order.
        MatchResult result = engine.submitOrder(buyOrder);

        // Confirm exactly one trade happened.
        assertEquals(1, result.getTrades().size());

        // Confirm the cheaper sell order matched first.
        assertEquals(2L, result.getTrades().get(0).getSellOrderId());

        // Confirm the trade happened at the cheaper resting price.
        assertEquals(new BigDecimal("99.00"), result.getTrades().get(0).getPrice());
    }

    // TEST THAT PRICES THAT DO NOT CROSS DO NOT MATCH
    @Test
    void ordersDoNotMatchWhenPricesDoNotCross() {

        // Create a fresh matching engine.
        MatchingEngine engine = new MatchingEngine();

        // Create a sell order asking for 110.
        Order sellOrder = new Order("AAPL", OrderSide.SELL, new BigDecimal("110.00"), 10);
        sellOrder.setId(1L);

        // Submit the sell order.
        engine.submitOrder(sellOrder);

        // Create a buy order only willing to pay 100.
        Order buyOrder = new Order("AAPL", OrderSide.BUY, new BigDecimal("100.00"), 10);
        buyOrder.setId(2L);

        // Submit the buy order.
        MatchResult result = engine.submitOrder(buyOrder);

        // Confirm no trade happened.
        assertTrue(result.getTrades().isEmpty());

        // Confirm both orders are still open.
        assertEquals(OrderStatus.OPEN, sellOrder.getStatus());
        assertEquals(OrderStatus.OPEN, buyOrder.getStatus());

        // Confirm both sides now have resting orders.
        assertEquals(1, engine.getOrderBook("AAPL").getSellOrdersSnapshot().size());
        assertEquals(1, engine.getOrderBook("AAPL").getBuyOrdersSnapshot().size());
    }
}
