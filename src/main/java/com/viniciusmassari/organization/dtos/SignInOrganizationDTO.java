package com.viniciusmassari.organization.dtos;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public record SignInOrganizationDTO(
        @Schema(examples = "John Doe")
        @NotBlank(message = "Campo nome do proprietário não pode estar vazio ou conter apenas espaços")
        String owner_name,

        @NotBlank(message = "Campo email não pode estar vazio ou conter apenas espaços")
        @Email(message = "Campo email precisa ser um email válido")
        @Schema(examples = "johndoe@email.com")
        String email,

        @NotBlank(message = "Campo senha não pode estar vazia ou conter apenas espaços")
        @Size(min = 8, message = "Senha precisa ter no mínimo 8 caracteres")
        @Schema(examples = "12345678")
        String password,

        @NotBlank(message = "Campo telefone não pode estar vazio ou conter apenas espaços")
        @Schema(examples = "+5521999999999")

        @Pattern(regexp = "^(?:(?:\\+?55\\s?)?\\(?[1-9][0-9]\\)?\\s?)?(?:9[0-9]{4}|[2-5][0-9]{3})-?[0-9]{4}$", message = "número de telefone não segue o padrão correto")
        String phone_number,

        @NotBlank(message = "Campo endereço não pode estar vazio ou conter apenas espaços")
        @Schema(examples = "Some address")
        String address,

        @NotBlank(message = "Campo cep não pode estar vazio ou conter apenas espaços")
        @Schema(examples = "111111")
        String cep
) {
}
