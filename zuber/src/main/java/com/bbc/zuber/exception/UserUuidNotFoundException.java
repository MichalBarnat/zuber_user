package com.bbc.zuber.exception;

import java.util.UUID;

public class UserUuidNotFoundException extends RuntimeException {

    private static final String ERROR_MESSAGE = "User with uuid: %s not found!";

    public UserUuidNotFoundException(UUID uuid) {
        super(String.format(ERROR_MESSAGE, uuid));
    }
}
