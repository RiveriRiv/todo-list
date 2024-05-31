package com.todo.service.impl;

import com.todo.data.ItemDto;
import com.todo.data.Status;
import com.todo.mapper.ItemMapper;
import com.todo.model.Item;
import com.todo.repository.ItemRepository;
import com.todo.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public long createItem(ItemDto itemDto) {
        Item item = ItemMapper.INSTANCE.itemDtoToItem(itemDto);

        return itemRepository.save(item).getId();
    }

    @Override
    public List<ItemDto> getAllItems(boolean isNotDone) {
        List<Item> itemList = isNotDone ? itemRepository.findAllByStatusNot(Status.NOT_DONE) :
                (List<Item>) itemRepository.findAll();

        return itemList.stream()
                .map(ItemMapper.INSTANCE::itemToItemDto)
                .toList();
    }

    @Override
    public ItemDto getItem(Long id) {
        Item item = fetchItem(id);

        return ItemMapper.INSTANCE.itemToItemDto(item);
    }

    @Override
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
    public void updateDescription(Long id, String description) {
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
                return new IllegalStateException("Item not found");
            });
    }

    private void updateAndSaveStatus(Item item, Status status) {
        item.setStatus(status);
        itemRepository.save(item);
    }

    private void updateAndSaveDescription(Item item, String description) {
        item.setDescription(description);
        itemRepository.save(item);
    }

    private boolean isNotValidForUpdate(Item item) {
        return item.getStatus().equals(Status.PAST_DUE);
    }
}
