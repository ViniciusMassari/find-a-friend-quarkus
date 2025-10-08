package com.viniciusmassari;

import com.viniciusmassari.organization.controllers.OrganizationController;
import com.viniciusmassari.organization.dtos.LoginOrganizationRequestDTO;
import com.viniciusmassari.organization.dtos.LoginOrganizationResponseDTO;
import com.viniciusmassari.organization.dtos.SignInOrganizationDTO;
import com.viniciusmassari.organization.entities.OrganizationEntity;
import com.viniciusmassari.organization.repositories.OrganizationRepository;
import com.viniciusmassari.resources.PostgresResource;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.logging.Log;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.callback.QuarkusTestBeforeEachCallback;
import io.quarkus.test.junit.callback.QuarkusTestMethodContext;
import io.restassured.http.ContentType;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@QuarkusTest
@QuarkusTestResource(PostgresResource.class)
public class OrganizationResourceTests {
    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    OrganizationController controller;


    @Test
    @TestTransaction
    public void should_create_a_new_organization() {
        SignInOrganizationDTO newOrganization = new SignInOrganizationDTO("John Doe", "johndoe@email.com", "12345678", "+5511955302187", "rua das antas", "7896000");
        try(Response response = this.controller.signin_org(newOrganization)){
            assert response.getStatus() == 201;
        }
    }

    @Test
    @DisplayName("Should login an organization")
    @TestTransaction
    public void should_login_an_organization() {
        String password = "12345678";
        OrganizationEntity.addNewOrganization("John Doe", "johndoe@email.com", BcryptUtil.bcryptHash(password, 8), "+5511955302187", "7896-000", "rua das antas");
        try (Response response = this.controller.login_org(new LoginOrganizationRequestDTO("johndoe@email.com", "12345678"))){
            assert response.getStatus() == 200;
            assert (LoginOrganizationResponseDTO) response.getEntity() != null;
        }
    }
}
