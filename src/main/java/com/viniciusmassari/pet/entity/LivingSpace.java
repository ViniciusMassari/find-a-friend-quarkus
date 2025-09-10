package com.viniciusmassari.pet.entity;

public enum LivingSpace {
    SMALL(1),
    MEDIUM(2),
    WIDE(3);

    public int value;

    LivingSpace(int value) {
        this.value = value;
    }
}
