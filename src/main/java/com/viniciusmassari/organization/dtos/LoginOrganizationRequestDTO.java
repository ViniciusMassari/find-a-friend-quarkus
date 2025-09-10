package com.viniciusmassari.organization.dtos;

import jakarta.validation.constraints.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record LoginOrganizationRequestDTO(@Schema(examples = "johndoe@email.com")  @NotBlank(message = "Campo email não pode estar vazio") @Email(message = "Campo email precisa conter um email válido") String email, @Schema(required = true,examples = "12345678") @NotBlank(message = "Campo senha não pode estar vazio") @Size(min = 8,message = "A senha precisa ter 8 caracteres no mínimo") String password) {
}
