package com.ibrahim.orderflow.model;

// REPRESENTS THE CURRENT LIFECYCLE STATE OF AN ORDER
public enum OrderStatus {

    // The order is active and waiting to be matched.
    OPEN,

    // The order has been partially matched but still has remaining quantity.
    PARTIALLY_FILLED,

    // The order has been fully matched.
    FILLED,

    // The order was cancelled before being fully matched.
    CANCELLED
}