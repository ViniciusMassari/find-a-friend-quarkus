package com.viniciusmassari.exceptions;

public class OrganizationNotFound extends RuntimeException {
    public OrganizationNotFound(String message) {
        super(message);
    }
}
