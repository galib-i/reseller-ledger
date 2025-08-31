package com.galibi.resellerledger.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.galibi.resellerledger.dto.CreateItemDto;
import com.galibi.resellerledger.dto.UpdateItemDto;
import com.galibi.resellerledger.entities.Item;
import com.galibi.resellerledger.entities.User;
import com.galibi.resellerledger.repositories.ItemRepository;
import com.galibi.resellerledger.service.exception.ItemNotFoundException;


@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> getItems() {
        User currentUser = getCurrentUser();
        List<Item> items = itemRepository.findByUserAndDeletedAtIsNull(currentUser);

        return items;
    }

    public Optional<Item> getItemById(Long id) {
        User currentUser = getCurrentUser();
        return itemRepository.findByIdAndUserAndDeletedAtIsNull(id, currentUser);
    }

    public Item createItem(CreateItemDto itemDto) {
        User currentUser = getCurrentUser();
        Item newItem = new Item(currentUser, itemDto.name(), itemDto.description(), itemDto.brand(),
                itemDto.size(), itemDto.condition());

        return itemRepository.save(newItem);
    }

    public Item updateItem(Long id, UpdateItemDto itemDto) {
        User currentUser = getCurrentUser();
        Item existingItem = itemRepository.findByIdAndUserAndDeletedAtIsNull(id, currentUser)
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + id));

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

        return itemRepository.save(existingItem);
    }

    public void deleteItem(Long id) {
        User currentUser = getCurrentUser();
        Item itemToDelete = itemRepository.findByIdAndUserAndDeletedAtIsNull(id, currentUser)
                .orElseThrow(() -> new ItemNotFoundException("Item not found with id: " + id));

        itemToDelete.setDeletedAt(LocalDateTime.now());
        itemRepository.save(itemToDelete);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
