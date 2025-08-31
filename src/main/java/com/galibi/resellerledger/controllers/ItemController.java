package com.galibi.resellerledger.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.galibi.resellerledger.dto.CreateItemDto;
import com.galibi.resellerledger.dto.UpdateItemDto;
import com.galibi.resellerledger.entities.Item;
import com.galibi.resellerledger.repositories.ItemRepository;
import com.galibi.resellerledger.service.ItemService;
import com.galibi.resellerledger.service.exception.ItemNotFoundException;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    public ItemController(ItemRepository itemRepository, ItemService itemService) {
        this.itemRepository = itemRepository;
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        List<Item> items = itemService.getItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody CreateItemDto itemDto) {
        Item savedItem = itemService.createItem(itemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id,
            @RequestBody UpdateItemDto itemDto) {
        try {
            Item updatedItem = itemService.updateItem(id, itemDto);
            return ResponseEntity.ok(updatedItem);

        } catch (ItemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        try {
            itemService.deleteItem(id);
            return ResponseEntity.noContent().build();

        } catch (ItemNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
