package com.todo.service.impl;

import com.todo.data.ItemDto;
import com.todo.data.Status;
import com.todo.exception.ItemNotFoundException;
import com.todo.model.Item;
import com.todo.repository.ItemRepository;
import com.todo.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final ModelMapper modelMapper;

    @Override
    public long createItem(ItemDto itemDto) {
        Item item = modelMapper.map(itemDto, Item.class);
        item.setCreationDate(LocalDateTime.now());

        if (Status.DONE.equals(item.getStatus())) {
            item.setMarkDate(LocalDateTime.now());
        }

        long id = itemRepository.save(item).getId();

        log.debug("New item was created with id={}", id);

        return id;
    }

    @Override
    public List<ItemDto> getAllItems(boolean isNotDone) {
        List<Item> itemList = isNotDone ? itemRepository.findAllByStatus(Status.NOT_DONE) :
                (List<Item>) itemRepository.findAll();

        return itemList.stream()
                .map(item -> modelMapper.map(item, ItemDto.class))
                .toList();
    }

    @Override
    public ItemDto getItem(Long id) {
        Item item = fetchItem(id);

        return modelMapper.map(item, ItemDto.class);
    }

    @Override
    @Transactional
    public void updateStatus(Long id, Status status) {
        Item item = fetchItem(id);

        if (isNotValidForUpdate(item)) {
            log.error("Item with id={} cannot be updated due to its current status", id);
            throw new IllegalStateException("Item cannot be updated due to its current status");
        }

        if (!item.getStatus().equals(status)) {
            updateAndSaveStatus(item, status);
        }
    }

    @Override
    @Transactional
    public void updateDescription(Long id, String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }

        Item item = fetchItem(id);

        if (isNotValidForUpdate(item)) {
            log.error("Item with id={} cannot be updated due to its current status", id);
            throw new IllegalStateException("Item cannot be updated due to its current status");
        }

        if (!item.getDescription().equals(description)) {
            updateAndSaveDescription(item, description);
        }
    }

    private Item fetchItem(long id) {
        return itemRepository.findById(id).orElseThrow(() -> {
            log.error("Item with id={} not found", id);
            return new ItemNotFoundException("Item not found");
        });
    }

    private void updateAndSaveStatus(Item item, Status status) {
        item.setStatus(status);

        if (Status.DONE.equals(item.getStatus())) {
            item.setMarkDate(LocalDateTime.now());
        }

        itemRepository.save(item);

        log.debug("Item with id={} updated with status={}", item.getId(), status);
    }

    private void updateAndSaveDescription(Item item, String description) {
        item.setDescription(description);
        itemRepository.save(item);

        log.debug("Item with id={} updated with description={}", item.getId(), description);
    }

    private boolean isNotValidForUpdate(Item item) {
        return item.getStatus().equals(Status.PAST_DUE);
    }
}
