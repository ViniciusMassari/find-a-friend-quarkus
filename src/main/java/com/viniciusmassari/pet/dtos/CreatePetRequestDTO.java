package com.viniciusmassari.pet.dtos;

import com.viniciusmassari.pet.entity.LivingSpace;
import com.viniciusmassari.pet.entity.PetEnergy;
import com.viniciusmassari.pet.entity.PetIndependence;
import com.viniciusmassari.pet.entity.PetSize;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


public record CreatePetRequestDTO(
        @Schema(examples = "spike",required = true)
        @NotBlank(message = "Campo nome não pode estar vazia ou conter apenas espaços")
        String name,

        @NotBlank(message = "Campo descrição não pode estar vazia ou conter apenas espaços")
        @Schema(examples = {"Alegre, brincalhão, espaçoso, ótima relação com crianças", "Resgatado das ruas a pouco tempo e em busca de uma nova família"}, required = true)
        String description,

        @Schema(examples = "10")
        @NotBlank(message = "Campo idade não pode estar vazia ou conter apenas espaços")
        short age,

        @NotBlank(message = "Campo espaço não pode estar vazia ou conter apenas espaços")
        @Schema(required = true)

        LivingSpace living_space,

        @NotBlank(message = "Campo independência não pode estar vazia ou conter apenas espaços")
        @Schema(required = true)
        PetIndependence independence,

        @Schema(required = true)
        @NotBlank(message = "Campo energia não pode estar vazia ou conter apenas espaços")
        PetEnergy energy,

        @Schema(required = true)
        @NotBlank(message = "Campo tamanho do pet não pode estar vazia ou conter apenas espaços")
        PetSize pet_size,

        @NotBlank(message = "Campo requisitos não pode estar vazia ou conter apenas espaços")
        @Schema(examples = "Bom espaço para se exercitar, boa alimentação, bom tratamento, paciência, brinquedos para se distrair", required = true)
        String requirements) {
}
