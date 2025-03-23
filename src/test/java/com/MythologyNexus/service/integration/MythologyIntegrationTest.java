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


    /*@Test
    void testMythologyCrudOperations() {
        Mythology mythology = new Mythology();
        mythology.setName("Norse Mythology");
        mythology.setDescription("Gods and heroes from Norse region");

        ResponseEntity<Mythology> createResponse = restTemplate.postForEntity(getBaseUrl(), mythology, Mythology.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        Mythology createMythology = createResponse.getBody();
        assertNotNull(createMythology);
        Long id = createMythology.getId();
        assertNotNull(id);

        ResponseEntity<Mythology> getResponse = restTemplate.getForEntity(getBaseUrl() + "/{id}", Mythology.class, id);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        Mythology fetchedMythology = getResponse.getBody();
        assertNotNull(fetchedMythology);
        assertEquals("Norse Mythology", fetchedMythology.getName());
        assertEquals("Gods and heroes from Norse region", fetchedMythology.getDescription());

        fetchedMythology.setDescription("Updated Norse Mythology Description");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Mythology> updateEntity = new HttpEntity<>(fetchedMythology, headers);
        ResponseEntity<Mythology> updateResponse = restTemplate.exchange(
                getBaseUrl() + "/{id}", HttpMethod.PATCH, updateEntity, Mythology.class, id);
        assertEquals(HttpStatus.OK,updateResponse.getStatusCode());
        Mythology updatedMythology = updateResponse.getBody();
        assertNotNull(updatedMythology);
        assertEquals("Updated Norse Mythology Description", updatedMythology.getDescription());

        ResponseEntity<Mythology[]> allResponse = restTemplate.getForEntity(getBaseUrl(), Mythology[].class);
        assertEquals(HttpStatus.OK,allResponse.getStatusCode());
        Mythology[] mythologies = allResponse.getBody();
        assertNotNull(mythologies);
        assertThat(mythologies.length, greaterThanOrEqualTo(1));

        restTemplate.delete(getBaseUrl() + "/{id}",id);
        ResponseEntity<Mythology> afterDeleteResponse = restTemplate.getForEntity(getBaseUrl() + "/{id}", Mythology.class,id);
        assertEquals(HttpStatus.NOT_FOUND,afterDeleteResponse.getStatusCode());
    }*/
}
