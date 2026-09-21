package com.teju.library.exception;

/**
 * Thrown when a requested resource (book, member, record) does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
