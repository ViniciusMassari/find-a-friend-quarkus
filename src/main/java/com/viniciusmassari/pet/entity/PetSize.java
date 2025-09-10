package com.viniciusmassari.pet.entity;

public enum PetSize {
    SMALL(1),
    MEDIUM(2),
    BIG(3);

    private final int value;

    PetSize(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
