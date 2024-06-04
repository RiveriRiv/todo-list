package com.todo.service;

import com.todo.data.ItemDto;
import com.todo.data.Status;
import com.todo.exception.ItemNotFoundException;
import com.todo.model.Item;
import com.todo.repository.ItemRepository;
import com.todo.service.impl.ItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ItemServiceImpl itemService;

    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    public void setUp() {
        item = new Item();
        item.setId(1L);
        item.setDescription("Test Item");
        item.setStatus(Status.NOT_DONE);
        item.setCreationDate(LocalDateTime.now());

        itemDto = new ItemDto();
        itemDto.setDescription("Test Item DTO");
        itemDto.setStatus(Status.NOT_DONE);
        itemDto.setDueDate(LocalDateTime.now().plusDays(1));
    }

    @Test
    public void createItem_Success() {
        when(modelMapper.map(any(ItemDto.class), any(Class.class))).thenReturn(item);
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        long id = itemService.createItem(itemDto);

        assertEquals(1L, id);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void getAllItems_ReturnsList() {
        when(itemRepository.findAllByStatus(Status.NOT_DONE)).thenReturn(Collections.singletonList(item));
        when(modelMapper.map(any(Item.class), any(Class.class))).thenReturn(itemDto);

        List<ItemDto> items = itemService.getAllItems(true);

        assertEquals(1, items.size());
        assertEquals("Test Item DTO", items.get(0).getDescription());
    }

    @Test
    public void getItem_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(modelMapper.map(any(Item.class), any(Class.class))).thenReturn(itemDto);

        ItemDto result = itemService.getItem(1L);

        assertEquals("Test Item DTO", result.getDescription());
    }

    @Test
    public void getItem_NotFound() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> itemService.getItem(1L));
    }

    @Test
    public void updateStatus_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        itemService.updateStatus(1L, Status.DONE);

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void updateDescription_Success() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        itemService.updateDescription(1L, "New Description");

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void updateDescription_NullDescription() {
        assertThrows(IllegalArgumentException.class, () -> itemService.updateDescription(1L, null));
    }

    @Test
    public void updateStatus_InvalidStatus() {
        item.setStatus(Status.PAST_DUE);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(IllegalStateException.class, () -> itemService.updateStatus(1L, Status.DONE));
    }

    @Test
    public void updateDescription_InvalidStatus() {
        item.setStatus(Status.PAST_DUE);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(IllegalStateException.class, () -> itemService.updateDescription(1L, "New Description"));
    }
}