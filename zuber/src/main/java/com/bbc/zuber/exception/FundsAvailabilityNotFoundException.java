package com.bbc.zuber.exception;

public class FundsAvailabilityNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Funds availability with id: %d not found!";

    public FundsAvailabilityNotFoundException(long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
