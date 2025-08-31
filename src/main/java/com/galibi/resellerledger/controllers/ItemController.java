package com.galibi.resellerledger.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.galibi.resellerledger.entities.User;
import com.galibi.resellerledger.repositories.ItemRepository;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getItems() {
        User currentUser = getCurrentUser();
        List<Item> items = itemRepository.findByUserAndDeletedAtIsNull(currentUser);

        return ResponseEntity.ok(items);

    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        return itemRepository.findByIdAndUserAndDeletedAtIsNull(id, currentUser)
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody CreateItemDto itemDto) {
        User currentUser = getCurrentUser();
        Item newItem = new Item(currentUser, itemDto.name(), itemDto.description(), itemDto.brand(),
                itemDto.size(), itemDto.condition());

        Item savedItem = itemRepository.save(newItem);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id,
            @RequestBody UpdateItemDto itemDto) {
        User currentUser = getCurrentUser();
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isEmpty()
                || !optionalItem.get().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Item existingItem = optionalItem.get();

        if (itemDto.name() != null) {
            existingItem.setName(itemDto.name());
        }
        if (itemDto.description() != null) {
            existingItem.setDescription(itemDto.description());
        }
        if (itemDto.brand() != null) {
            existingItem.setBrand(itemDto.brand());
        }
        if (itemDto.size() != null) {
            existingItem.setSize(itemDto.size());
        }
        if (itemDto.condition() != null) {
            existingItem.setCondition(itemDto.condition());
        }

        Item updatedItem = itemRepository.save(existingItem);

        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        User currentUser = getCurrentUser();
        Optional<Item> optionalItem = itemRepository.findById(id);

        if (optionalItem.isEmpty()
                || !optionalItem.get().getUser().getId().equals(currentUser.getId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Item itemToDelete = optionalItem.get();
        itemToDelete.setDeletedAt(LocalDateTime.now());

        itemRepository.save(itemToDelete);

        return ResponseEntity.noContent().build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

}
