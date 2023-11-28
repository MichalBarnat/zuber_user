package com.bbc.zuber.exception;

public class RideRequestNotFoundException extends RuntimeException{
    private Long id;

    public RideRequestNotFoundException(Long id) {
        this.id = id;
    }
}
