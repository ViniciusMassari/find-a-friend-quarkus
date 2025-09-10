package com.viniciusmassari.organization.usecases;

import com.viniciusmassari.exceptions.OrganizationNotFound;
import com.viniciusmassari.organization.dtos.LoginOrganizationRequestDTO;
import com.viniciusmassari.organization.dtos.LoginOrganizationResponseDTO;
import com.viniciusmassari.organization.entities.OrganizationEntity;
import com.viniciusmassari.organization.repositories.OrganizationRepository;
import com.viniciusmassari.organization.repositories.dtos.LoginOrganizationReflection;
import com.viniciusmassari.utils.TokenUtil;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class LoginUseCase {
    @Inject
    TokenUtil tokenUtil;
    @Inject
    OrganizationRepository organizationRepository;
    public LoginOrganizationResponseDTO execute(LoginOrganizationRequestDTO loginOrganization){
       Optional<LoginOrganizationReflection> organization = this.organizationRepository.find("email",loginOrganization.email()).project(LoginOrganizationReflection.class).stream().findFirst();
       if(organization.isEmpty()){
           throw new OrganizationNotFound("Organização não encontrada ou não cadastrada, verifique os dados e tente novamente");
       }
        String token = this.tokenUtil.createToken(organization.get().id.toString());
return new LoginOrganizationResponseDTO(token);
    }
}
