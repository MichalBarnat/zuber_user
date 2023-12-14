package com.bbc.zuber.exception;

public class UserNotFoundException extends RuntimeException{
    private Long id;

    public UserNotFoundException(Long id) {
        this.id = id;
    }


}
