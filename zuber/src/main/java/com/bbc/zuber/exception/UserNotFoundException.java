package com.bbc.zuber.exception;

public class UserNotFoundException extends RuntimeException {
    private static final String ERROR_MESSAGE = "User with id: %d not found!";

    public UserNotFoundException(long id) {
        super(String.format(ERROR_MESSAGE, id));
    }
}
