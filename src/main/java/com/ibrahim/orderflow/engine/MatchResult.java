package com.ibrahim.orderflow.engine;

import com.ibrahim.orderflow.model.Order;
import com.ibrahim.orderflow.model.Trade;

import java.util.Collections;
import java.util.List;

// HOLDS THE RESULT OF SUBMITTING ONE ORDER TO THE MATCHING ENGINE
public class MatchResult {

    // Store the submitted order after any fills.
    private final Order incomingOrder;

    // Store every trade created from this order submission.
    private final List<Trade> trades;

    // CREATE A MATCH RESULT
    public MatchResult(Order incomingOrder, List<Trade> trades) {

        // Store the incoming order after matching.
        this.incomingOrder = incomingOrder;

        // Store an immutable view of the generated trades.
        this.trades = Collections.unmodifiableList(trades);
    }

    public Order getIncomingOrder() {
        return incomingOrder;
    }

    public List<Trade> getTrades() {
        return trades;
    }

    public boolean hasTrades() {
        return !trades.isEmpty();
    }
}