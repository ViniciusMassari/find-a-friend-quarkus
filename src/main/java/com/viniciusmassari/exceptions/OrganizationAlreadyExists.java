package com.viniciusmassari.exceptions;

public class OrganizationAlreadyExists extends RuntimeException {
    public OrganizationAlreadyExists(String message) {
        super(message);
    }
}
