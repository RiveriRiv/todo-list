package com.todo.service;

import com.todo.data.ItemDto;
import com.todo.data.Status;

import java.util.List;

public interface ItemService {

    long createItem(ItemDto itemDto);

    List<ItemDto> getAllItems(boolean isNotDone);

    ItemDto getItem(Long id);

    void updateStatus(Long id, Status status);

    void updateDescription(Long id, String description);
}
