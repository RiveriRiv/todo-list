package com.todo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.data.ItemDto;
import com.todo.data.Status;
import com.todo.exception.ItemNotFoundException;
import com.todo.service.ItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Test
    public void createItem_Success() throws Exception {
        ItemDto itemDto = new ItemDto("Test Description", Status.NOT_DONE,
                null, LocalDateTime.now().plusDays(1), null);
        long newId = 1L;

        when(itemService.createItem(any(ItemDto.class))).thenReturn(newId);

        mockMvc.perform(post("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Test Description\",\"status\":\"NOT_DONE\",\"dueDate\":\"2024-06-05T12:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/item/" + newId)));
    }

    @Test
    public void createItem_InvalidData() throws Exception {
        ItemDto itemDto = new ItemDto();

        mockMvc.perform(post("/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateDescription_Success() throws Exception {
        long id = 1L;
        String newDescription = "Updated Description";

        doNothing().when(itemService).updateDescription(id, newDescription);

        mockMvc.perform(put("/item/description/{id}", id)
                        .param("description", newDescription))
                .andExpect(status().isOk());
    }

    @Test
    public void updateDescription_NotFound() throws Exception {
        long id = 1L;
        String newDescription = "Updated Description";

        doThrow(new ItemNotFoundException("Item not found")).when(itemService).updateDescription(id, newDescription);

        mockMvc.perform(put("/item/description/{id}", id)
                        .param("description", newDescription))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateStatus_Success() throws Exception {
        long id = 1L;
        Status newStatus = Status.DONE;

        doNothing().when(itemService).updateStatus(id, newStatus);

        mockMvc.perform(put("/item/status/{id}", id)
                        .param("status", newStatus.name()))
                .andExpect(status().isOk());
    }

    @Test
    public void updateStatus_InvalidStatus() throws Exception {
        long id = 1L;
        String newStatus = "Some status";

        mockMvc.perform(put("/item/status/{id}", id)
                        .param("status", newStatus))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateStatus_NotFound() throws Exception {
        long id = 1L;
        Status newStatus = Status.DONE;

        doThrow(new ItemNotFoundException("Item not found")).when(itemService).updateStatus(id, newStatus);

        mockMvc.perform(put("/item/status/{id}", id)
                        .param("status", newStatus.name()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getItem_Success() throws Exception {
        long id = 1L;
        ItemDto itemDto = new ItemDto("Test Description", Status.NOT_DONE,
                null, LocalDateTime.now().plusDays(1), null);

        when(itemService.getItem(id)).thenReturn(itemDto);

        mockMvc.perform(get("/item/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.status").value(itemDto.getStatus().name()));
    }

    @Test
    public void getItem_NotFound() throws Exception {
        long id = 1L;

        when(itemService.getItem(id)).thenThrow(new ItemNotFoundException("Item not found"));

        mockMvc.perform(get("/item/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllItemList_Success() throws Exception {
        List<ItemDto> itemList = Arrays.asList(
                new ItemDto("Test Description 1", Status.NOT_DONE, null, LocalDateTime.now().plusDays(1), null),
                new ItemDto("Test Description 2", Status.NOT_DONE, null, LocalDateTime.now().plusDays(2), null),
                new ItemDto("Test Description 3", Status.DONE, null, LocalDateTime.now().plusDays(2), null)

        );

        when(itemService.getAllItems(true)).thenReturn(itemList);

        mockMvc.perform(get("/item")
                        .param("isNotDone", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(itemList.size()));
    }
}
