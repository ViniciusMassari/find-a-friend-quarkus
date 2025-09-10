package com.viniciusmassari.pet.entity;

public enum PetEnergy {
    LOW(1),
    MEDIUM(2),
    HIGH(3);

    public int value;

    PetEnergy(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
