package com.viniciusmassari.pet.entity;

public enum PetIndependence {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    public int value;

    PetIndependence(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
