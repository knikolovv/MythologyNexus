package com.MythologyNexus.service.integration;

import com.MythologyNexus.model.Mythology;
import com.MythologyNexus.repository.MythologyRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MythologyIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    MythologyRepo mythologyRepo;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/mythologies";
    }

    @AfterEach
    void tearDown() {
        mythologyRepo.deleteAll();
    }

    @Test
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
    }
}
