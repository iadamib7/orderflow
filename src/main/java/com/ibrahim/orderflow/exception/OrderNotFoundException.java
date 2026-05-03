package com.ibrahim.orderflow.exception;

// THROWN WHEN AN ORDER ID DOES NOT EXIST
public class OrderNotFoundException extends RuntimeException {

    // CREATE AN ORDER NOT FOUND ERROR WITH A MESSAGE
    public OrderNotFoundException(String message) {

        // Pass the message to RuntimeException.
        super(message);
    }
}