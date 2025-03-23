package com.MythologyNexus.service.integration;

import com.MythologyNexus.model.Mythology;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MythologyIntegrationTest {

    @LocalServerPort
    private int port;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/mythologies";
    }


    @Test
    void testCRUDMythologyOperations() {
        Mythology mythology = new Mythology();
        mythology.setName("Norse Mythology");
        mythology.setDescription("Gods and Monsters");

        Integer generatedId = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(mythology)
                .when()
                .post(getBaseUrl())
            .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Norse Mythology"))
                .body("description", equalTo("Gods and Monsters"))
                .extract()
                .path("id");

        Long id = generatedId.longValue();
        assertNotNull(id);

        RestAssured.given()
                .when()
                .get(getBaseUrl() + "/{id}", id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Norse Mythology"))
                .body("description", equalTo("Gods and Monsters"));

        mythology.setName("Greek Mythology");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(mythology)
                .when()
                .patch(getBaseUrl() + "/{id}", id)
                .then()
                .statusCode(200)
                .body("name",equalTo("Greek Mythology"));

        RestAssured.given()
                .when()
                .get(getBaseUrl() + "/{id}", id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Greek Mythology"));

        RestAssured.given()
                .when()
                .delete(getBaseUrl() + "/{id}",id)
                .then()
                .statusCode(204);

        RestAssured.given()
                .when()
                .get(getBaseUrl() + "/{id}", id)
                .then()
                .statusCode(404);
    }
}
