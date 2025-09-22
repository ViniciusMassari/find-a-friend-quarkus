package com.viniciusmassari;
import com.viniciusmassari.pet.entity.*;
import com.viniciusmassari.pet.usecases.CreatePetUseCase;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.security.TestSecurity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.viniciusmassari.organization.repositories.OrganizationRepository;
import com.viniciusmassari.pet.dtos.CreatePetRequestDTO;
import com.viniciusmassari.resources.PostgresResource;
import io.quarkiverse.wiremock.devservice.ConnectWireMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;

import java.security.Principal;
import java.util.UUID;

@ConnectWireMock
@QuarkusTest
public class PetResourceTest {
    private static final Logger LOG = Logger.getLogger(PetResourceTest.class);

    WireMock wiremock;

    @Inject
    OrganizationRepository organizationRepository;

    @InjectMock
    JsonWebToken jsonWebToken;


    @InjectMock
    CreatePetUseCase createPetUseCase;


    @Test
    @DisplayName("Should create a new pet (not testing the jwt authentication)")
    @TestSecurity(user = "expectedUser", roles = "user")
    public void create_pet() throws JsonProcessingException {
        PanacheMock.mock(PetEntity.class);

        CreatePetRequestDTO createPetRequest = new CreatePetRequestDTO(
                "Docker", "description", (short) 10,
                LivingSpace.SMALL, PetIndependence.HIGH, PetEnergy.HIGH, PetSize.SMALL,
                "requirements"
        );


        Mockito.when(jsonWebToken.getSubject()).thenReturn("expectedUser");
Mockito.doNothing().when(this.createPetUseCase).execute(createPetRequest,"expectedUser");

        given()
                .contentType(ContentType.JSON)
                .body(createPetRequest)
                .when()
                .post("/pet/")
                .then()
                .statusCode(201);
    }

}
