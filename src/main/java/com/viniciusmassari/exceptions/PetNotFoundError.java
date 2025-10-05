package com.viniciusmassari.exceptions;

public class PetNotFoundError extends RuntimeException {
    public PetNotFoundError(String message) {
        super(message);
    }
}
