package com.bbc.zuber.exception;

public class RideRequestNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "Ride request with id: %d not found!";

    public RideRequestNotFoundException(long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
