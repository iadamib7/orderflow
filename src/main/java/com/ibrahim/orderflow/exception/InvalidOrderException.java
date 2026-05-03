package com.ibrahim.orderflow.exception;

// THROWN WHEN AN ORDER REQUEST IS INVALID
public class InvalidOrderException extends RuntimeException {

    // CREATE AN INVALID ORDER ERROR WITH A MESSAGE
    public InvalidOrderException(String message) {

        // Pass the message to RuntimeException.
        super(message);
    }
}