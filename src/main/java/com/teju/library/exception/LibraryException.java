package com.teju.library.exception;

/**
 * Thrown for business-rule violations, e.g. borrowing a book with no
 * available copies or returning an already-returned record.
 */
public class LibraryException extends RuntimeException {

    public LibraryException(String message) {
        super(message);
    }
}
