package com.galibi.resellerledger.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.galibi.resellerledger.dto.CreateItemDto;
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

    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody CreateItemDto itemDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Item newItem = new Item(currentUser, itemDto.name(), itemDto.description(), itemDto.brand(),
                itemDto.size(), itemDto.condition());

        newItem.setUser(currentUser);
        Item savedItem = itemRepository.save(newItem);

        return ResponseEntity.status(201).body(savedItem);
    }
}
