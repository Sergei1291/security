package com.epam.esm.exception.tag;

public class TagNameAlreadyExistsException extends RuntimeException {

    public TagNameAlreadyExistsException(String message) {
        super(message);
    }

}