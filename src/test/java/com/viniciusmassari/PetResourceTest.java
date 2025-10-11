package com.viniciusmassari;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.viniciusmassari.organization.entities.OrganizationEntity;
import com.viniciusmassari.pet.controllers.PetController;
import com.viniciusmassari.pet.entity.*;
import com.viniciusmassari.pet.services.PetCacheService;
import com.viniciusmassari.pet.usecases.CreatePetUseCase;
import com.viniciusmassari.profiles.NoRateLimitTestProfile;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import com.viniciusmassari.pet.dto.CreatePetRequestDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import org.mockito.Mockito;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@QuarkusTest
@TestProfile(NoRateLimitTestProfile.class)
@TestHTTPEndpoint(PetController.class)
public class PetResourceTest {
    private static final Logger LOG = Logger.getLogger(PetResourceTest.class);


    @InjectMock
    PetCacheService petCache;

    @InjectMock
    JsonWebToken jsonWebToken;


    @InjectMock
    CreatePetUseCase createPetUseCase;


    @Test
    @DisplayName("Should create a new pet (not testing the jwt authentication)")
    @TestSecurity(user = "expectedUser", roles = "user")
    @TestTransaction
    public void create_pet() {
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
                .post()
                .then()
                .contentType(ContentType.JSON)
                .statusCode(201);
    }

    @Test
    @DisplayName("Should return a 400 status because a city was not provided")
    public void should_return_error_city_not_provided(){

        Map<String,String> params = new HashMap<>();
        params.put("city","");
        given().contentType(ContentType.JSON).queryParams(params).get().then().statusCode(400);
    }
    @Test
    @DisplayName("Should return a 400 status because a param does not correspond with the expected enums")
    public void should_return_error_unexpected_param_value(){

        Map<String,String> params = new HashMap<>();
        params.put("city","São Paulo");
        params.put("age","wrong value");
        given()
                .queryParams(params)
                .get()
                .then().statusCode(400);
    }


    @Test
    @DisplayName("Should return a list with two pets")
    public void should_return_a_list()  {
        List<PetEntity> pets = new ArrayList<>();
        pets.add(new PetEntity());
        pets.add(new PetEntity());

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
              .get()
              .then().statusCode(200).body("",notNullValue());
    }

    @Test
    @DisplayName("Should return cached response")
    @TestTransaction
    public void should_hit_cache()  {
        PetEntity pet = new PetEntity();
        pet.id = UUID.randomUUID();

        Mockito.when(this.petCache.get(pet.id.toString())).thenReturn(pet);

        given().log().ifValidationFails()
                .when().pathParam("id", pet.id.toString())
                .get("/{id}")
                .then().statusCode(200).body("",notNullValue()).header("X-Cache", equalTo("HIT"));



    }


    @Test
    @DisplayName("Should return a pet info")
    public void pet_info() {
        PetEntity pet = new PetEntity();
        pet.id = UUID.randomUUID();
        PanacheMock.mock(PetEntity.class);
        PetEntity.persist(pet);

        Mockito.when(PetEntity.findById(pet.id)).thenReturn(pet);

        given().log().ifValidationFails()
                .when().pathParam("id",pet.id.toString())
                .get("/{id}")
                .then().statusCode(200).contentType(ContentType.JSON).body("",notNullValue());
    }
    @Test
    @DisplayName("Pet should not exist")
    public void pet_info_fail() {
    var response = given()
                .when().pathParam("id", "wrongId")
                .get("/{id}")
                .then().statusCode(400).extract().asString();

    assertEquals("Pet solicitado não existe, verifique os dados e tente novamente", response);
    }




}
