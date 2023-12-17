package com.bbc.zuber.exception;

public class FundsAvailabilityNotFound extends RuntimeException {

    private static final String ERROR_MESSAGE = "Funds availability with id: %d not found!";

    public FundsAvailabilityNotFound(long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
