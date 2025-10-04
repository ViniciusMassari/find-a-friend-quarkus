package com.viniciusmassari;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.viniciusmassari.pet.entity.*;
import com.viniciusmassari.pet.usecases.CreatePetUseCase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.security.TestSecurity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.viniciusmassari.organization.repositories.OrganizationRepository;
import com.viniciusmassari.pet.dto.CreatePetRequestDTO;
import io.quarkiverse.wiremock.devservice.ConnectWireMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import org.mockito.Mockito;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@QuarkusTest
public class PetResourceTest {
    private static final Logger LOG = Logger.getLogger(PetResourceTest.class);


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
                "Docker", "description", Age.FILHOTE,
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
                .contentType(ContentType.JSON)
                .statusCode(201);
    }

    @Test
    @DisplayName("Should return a 400 status because a city was not provided")
    public void should_return_error_city_not_provided(){

        Map<String,String> params = new HashMap<>();
        params.put("city","");
        given().contentType(ContentType.JSON).queryParams(params).get("/pet").then().statusCode(400);
    }
    @Test
    @DisplayName("Should return a 400 status because a param does not correspond with the expected enums")
    public void should_return_error_unexpected_param_value(){

        Map<String,String> params = new HashMap<>();
        params.put("city","São Paulo");
        params.put("age","wrong value");
        given().queryParams(params).get("/pet").then().statusCode(400);
    }


    @Test
    @DisplayName("Should return a list with two animals")
    public void should_return_a_list() throws org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException {
        List<PetEntity> pets = new ArrayList<>();
        pets.add(new PetEntity());
        pets.add(new PetEntity());
        ObjectMapper mapper = new ObjectMapper();
        String expectedBody = mapper.writeValueAsString(pets);

        Map<String, String> params = new HashMap<>();
        params.put("city", "São Paulo");
        params.put("age","FILHOTE");

        PanacheMock.mock(PetEntity.class);
        PanacheQuery query = Mockito.mock(PanacheQuery.class);

        Mockito.when(PetEntity.find("city= :city and age= :age",params)).thenReturn(query);

      given()
              .queryParam("city","São Paulo")
              .queryParam("age",Age.FILHOTE.toString())
              .when()
              .get("/pet")
              .then().statusCode(200).body("",notNullValue());


    }

}
