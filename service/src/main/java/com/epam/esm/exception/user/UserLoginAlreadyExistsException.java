package com.epam.esm.exception.user;

public class UserLoginAlreadyExistsException extends RuntimeException {

    public UserLoginAlreadyExistsException(String message) {
        super(message);
    }

}