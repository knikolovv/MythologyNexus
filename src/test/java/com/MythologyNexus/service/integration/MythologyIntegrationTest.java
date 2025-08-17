package com.MythologyNexus.service.integration;

import com.MythologyNexus.model.Mythology;
import com.MythologyNexus.service.integration.config.TestSecurityConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class MythologyIntegrationTest {
    @LocalServerPort
    private int port;

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("mythology_test")
            .withUsername("mythology")
            .withPassword("password");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/mythologies";
    }

    @Test
    void testCRUDMythologyOperations() {
        Mythology mythology = new Mythology();
        mythology.setName("Norse Mythology");
        mythology.setDescription("Gods and Monsters");

        Integer generatedId = given()
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

        given()
                .when()
                .get(getBaseUrl() + "/{id}", id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Norse Mythology"))
                .body("description", equalTo("Gods and Monsters"));

        mythology.setName("Greek Mythology");
        given()
                .contentType(ContentType.JSON)
                .body(mythology)
                .when()
                .patch(getBaseUrl() + "/{id}", id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Greek Mythology"));

        given()
                .when()
                .get(getBaseUrl() + "/{id}", id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Greek Mythology"));

        given()
                .when()
                .delete(getBaseUrl() + "/{id}", id)
                .then()
                .statusCode(204);

        given()
                .when()
                .get(getBaseUrl() + "/{id}", id)
                .then()
                .statusCode(404);
    }
}
