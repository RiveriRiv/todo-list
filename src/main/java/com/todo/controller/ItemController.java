package com.todo.controller;

import com.todo.data.ItemDto;
import com.todo.data.Status;
import com.todo.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/item")
@AllArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<Void> createItem(@RequestBody ItemDto itemDto) {
        long id = itemService.createItem(itemDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<String> updateDescription(@PathVariable long id,
                                                    @RequestParam("description") String description) {
        itemService.updateDescription(id, description);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/description/{id}")
    public ResponseEntity<String> updateStatus(@PathVariable long id,
                                               @RequestParam("status") Status status) {
        itemService.updateStatus(id, status);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable long id) {
        ItemDto itemDto = itemService.getItem(id);

        return ResponseEntity.ok(itemDto);
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItemList(@RequestParam(required = false, defaultValue = "true")
                                                     boolean isNotDone) {
        List<ItemDto> itemDto = itemService.getAllItems(isNotDone);

        return ResponseEntity.ok(itemDto);
    }
}
