package com.viniciusmassari.pet.dto;

import com.viniciusmassari.pet.entity.Age;
import com.viniciusmassari.pet.entity.LivingSpace;
import com.viniciusmassari.pet.entity.PetEnergy;
import com.viniciusmassari.pet.entity.PetIndependence;
import com.viniciusmassari.pet.validation.ValueOfEnum;
import jakarta.validation.constraints.NotBlank;
import org.jboss.resteasy.reactive.RestQuery;

public record GetPetByFiltersDTO(@RestQuery() @NotBlank(message = "O parâmetro cidade não é opcional e precisa ser preenchido") String city, @ValueOfEnum(enumClass = LivingSpace.class) @RestQuery() String living_space, @ValueOfEnum(enumClass = PetEnergy.class) @RestQuery() String energy, @ValueOfEnum(enumClass = Age.class ) @RestQuery() String age, @ValueOfEnum(enumClass = PetIndependence.class) @RestQuery String independence) {
}
