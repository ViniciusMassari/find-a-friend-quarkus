package com.viniciusmassari.organization.repositories.dtos;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.Column;

import java.util.UUID;

@RegisterForReflection
public class LoginOrganizationReflection {
    public UUID id;
    public String email;
    public String password;

    public LoginOrganizationReflection(String email, UUID id, String password) {
        this.email = email;
        this.id = id;
        this.password = password;
    }
}
