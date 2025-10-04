package com.viniciusmassari.pet.usecases;

import com.viniciusmassari.exceptions.OrganizationNotFound;
import com.viniciusmassari.organization.entities.OrganizationEntity;
import com.viniciusmassari.pet.dto.CreatePetRequestDTO;
import com.viniciusmassari.pet.entity.PetEntity;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CreatePetUseCase {

    public void execute(CreatePetRequestDTO createPetRequestDTO, String organizationId) {
        PanacheQuery<OrganizationEntity> organizationQuery = OrganizationEntity.find("id", organizationId);
        Optional<OrganizationEntity> organization = organizationQuery.singleResultOptional();
        if (organization.isEmpty()) {
            throw new OrganizationNotFound("Organização não foi encontrada");
        }

        PetEntity.addNewPet(createPetRequestDTO, UUID.fromString(organizationId));
    }
}
