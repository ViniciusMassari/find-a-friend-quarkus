package com.viniciusmassari.pet.dto;

import com.viniciusmassari.pet.entity.PetEntity;

import java.util.List;

public record GetPetsByFiltersResponseDTO(List<PetEntity> pets) {
}
