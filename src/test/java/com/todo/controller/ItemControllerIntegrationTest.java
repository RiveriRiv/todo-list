package com.todo.controller;

import com.todo.Application;
import com.todo.data.ItemDto;
import com.todo.data.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {"java.security.manager=null"})
public class ItemControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    public void createItem_Success() {
        String json = "{\"description\":\"Test Description\",\"status\":\"NOT_DONE\",\"dueDate\":\"2024-06-05T12:00:00\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/item", request, Void.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);

        URI location = responseEntity.getHeaders().getLocation();
        assertNotNull(location);
    }

    @Test
    @DirtiesContext
    public void createItem_InvalidStatus() {
        String json = "{\"description\":\"Test Description\",\"status\":\"SOME_STATUS\",\"dueDate\":\"2024-06-05T12:00:00\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<Void> responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/item", request, Void.class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);

        URI location = responseEntity.getHeaders().getLocation();
        assertNotNull(location);
    }

    @Test
    @DirtiesContext
    public void updateDescription_Success() {
        createItem();

        long itemId = 1;
        String newDescription = "Updated Description";

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/item/description/{id}?description={description}",
                HttpMethod.PUT,
                null,
                String.class,
                itemId,
                newDescription);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void updateDescription_NotFound() {
        long itemId = 1;
        String newDescription = "Updated Description";

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/item/description/{id}?description={description}",
                HttpMethod.PUT,
                null,
                String.class,
                itemId,
                newDescription);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void updateStatus_Success() {
        createItem();

        long itemId = 1;
        Status newStatus = Status.DONE;

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/item/status/{id}?status={status}",
                HttpMethod.PUT,
                null,
                String.class,
                itemId,
                newStatus);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @DirtiesContext
    public void updateStatus_NotFound() {
        long itemId = 1;
        Status newStatus = Status.DONE;

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:" + port + "/item/status/{id}?status={status}",
                HttpMethod.PUT,
                null,
                String.class,
                itemId,
                newStatus);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    @DirtiesContext
    public void getItem_Success() {
        createItem();

        long itemId = 1;

        ResponseEntity<ItemDto> responseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/item/{id}",
                ItemDto.class,
                itemId);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
    }

    @Test
    @DirtiesContext
    public void getItemList_Success() {
        createItem();
        createItem();

        ResponseEntity<ItemDto[]> responseEntity = restTemplate.getForEntity(
                "http://localhost:" + port + "/item",
                ItemDto[].class);

        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        assertNotNull(responseEntity.getBody());
    }

     private void createItem() {
        String json = "{\"description\":\"Test Description\",\"status\":\"NOT_DONE\",\"dueDate\":\"2024-06-05T12:00:00\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        restTemplate.postForEntity("http://localhost:" + port + "/item", request, Void.class);
    }

}
