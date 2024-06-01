package com.todo.mapper;

import com.todo.data.ItemDto;
import com.todo.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    ItemDto itemToItemDto(Item item);

    Item itemDtoToItem(ItemDto itemDto);
}
