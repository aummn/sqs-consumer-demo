package com.aummn.sqs.exception;

public class StockException extends Exception {

    // Default constructor
    public StockException() {
        super();
    }

    // Constructor with a custom message
    public StockException(String message) {
        super(message);
    }

    // Constructor with a custom message and a cause
    public StockException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with a cause
    public StockException(Throwable cause) {
        super(cause);
    }
}
