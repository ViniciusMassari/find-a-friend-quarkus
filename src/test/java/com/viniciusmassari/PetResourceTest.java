package com.viniciusmassari;

import com.viniciusmassari.organization.controllers.OrganizationController;
import com.viniciusmassari.organization.dtos.LoginOrganizationRequestDTO;
import com.viniciusmassari.organization.dtos.SignInOrganizationDTO;
import com.viniciusmassari.organization.entities.OrganizationEntity;
import com.viniciusmassari.pet.dtos.CreatePetRequestDTO;
import com.viniciusmassari.pet.entity.LivingSpace;
import com.viniciusmassari.pet.entity.PetEnergy;
import com.viniciusmassari.pet.entity.PetIndependence;
import com.viniciusmassari.pet.entity.PetSize;
import com.viniciusmassari.resources.PostgresResource;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@QuarkusTestResource(PostgresResource.class)
public class PetResourceTest {
    private static final Logger LOG = Logger.getLogger(PetResourceTest.class);

    @Test
    @DisplayName("Should create a new pet")
    @TestTransaction
    public void create_pet(){
        SignInOrganizationDTO newOrganization = new SignInOrganizationDTO("John Doe", "johndoe@email.com", "12345678", "+5511955302187", "rua das antas", "7896-000");
        given().body(newOrganization).contentType(ContentType.JSON).post("http://localhost:8080/organization/signin").then();
     var response =   given().body(new LoginOrganizationRequestDTO("johndoe@email.com", "12345678")).contentType(ContentType.JSON).post("http://localhost:8080/organization/login").then().extract().body();


    }
}
