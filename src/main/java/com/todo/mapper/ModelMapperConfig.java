package com.todo.mapper;

import com.todo.data.ItemDto;
import com.todo.model.Item;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(ItemDto.class, Item.class)
                .addMappings(mapping -> mapping.skip(Item::setId));

        modelMapper.typeMap(Item.class, ItemDto.class);

        return modelMapper;
    }
}
