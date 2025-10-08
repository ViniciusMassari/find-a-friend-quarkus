package com.viniciusmassari;

import com.viniciusmassari.profiles.RateLimitTestProfile;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@TestProfile(RateLimitTestProfile.class)
public class PetResourceRateLimitTest {
    @Test
    @DisplayName("Should active rate limit")
    public void pet_info_fail() {

        for (int i = 0; i <= 2; i++) {
            var response = given()
                    .when()
                    .get("/pet/" + "wrongid")
                    .then().statusCode(400);
        }
        var response = given()
                .when()
                .get("/pet/" + "wrongid")
                .then().statusCode(429);

    }
}
