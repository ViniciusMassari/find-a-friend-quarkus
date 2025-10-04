package com.viniciusmassari.pet.entity;

public enum Age {
    FILHOTE(1),
    ADULTO(2),
    IDOSO(3);

    public final int value;

    Age(int value) {
        this.value = value;
    }
}
