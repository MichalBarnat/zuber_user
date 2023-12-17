package com.bbc.zuber.exception;

import java.util.UUID;

public class FundsAvailabilityUuidNotFound extends RuntimeException {

    private static final String ERROR_MESSAGE = "Funds availability with uuid: %s not found!";

    public FundsAvailabilityUuidNotFound(UUID uuid) {
        super(String.format(ERROR_MESSAGE, uuid));
    }
}
