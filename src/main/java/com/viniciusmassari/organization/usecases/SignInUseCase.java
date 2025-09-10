package com.viniciusmassari.organization.usecases;

import com.viniciusmassari.exceptions.OrganizationAlreadyExists;
import com.viniciusmassari.exceptions.dto.SignInOrganizationResponseDTO;
import com.viniciusmassari.organization.dtos.SignInOrganizationDTO;
import com.viniciusmassari.organization.entities.OrganizationEntity;
import com.viniciusmassari.organization.repositories.OrganizationRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.ext.Provider;

import java.util.Optional;

@Provider
public class SignInUseCase {

    @Inject
    OrganizationRepository organizationRepository;

    public void execute(SignInOrganizationDTO signInOrganizationDTO){
     Optional<OrganizationEntity> organization =  this.organizationRepository.find("email", signInOrganizationDTO.email()).stream().findFirst();
if(organization.isPresent()){
    throw new OrganizationAlreadyExists("Organização já cadastrada no sistema");
}

OrganizationEntity.addNewOrganization(signInOrganizationDTO.owner_name(),signInOrganizationDTO.email(), signInOrganizationDTO.password(), signInOrganizationDTO.phone_number(), signInOrganizationDTO.cep(), signInOrganizationDTO.address());
    }
}
