package com.galibi.resellerledger.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.galibi.resellerledger.dto.CreateItemDto;
import com.galibi.resellerledger.dto.UpdateItemDto;
import com.galibi.resellerledger.entities.Item;
import com.galibi.resellerledger.entities.User;
import com.galibi.resellerledger.repositories.ItemRepository;
import com.galibi.resellerledger.service.exception.ItemNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

  @Mock
  private ItemRepository itemRepository;

  @InjectMocks
  private ItemService itemService;

  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User();
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(testUser);

    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void getItems_shouldReturnItemList_whenUserHasItems() {
    Item item1 = new Item();
    Item item2 = new Item();
    List<Item> expectedItems = List.of(item1, item2);

    when(itemRepository.findByUserAndDeletedAtIsNull(testUser)).thenReturn(expectedItems);

    List<Item> actualItems = itemService.getItems();

    assertNotNull(actualItems);
    assertEquals(2, actualItems.size());
    assertEquals(expectedItems, actualItems);
  }

  @Test
  void getItems_shouldReturnEmptyList_whenUserHasNoItems() {
    when(itemRepository.findByUserAndDeletedAtIsNull(testUser))
        .thenReturn(java.util.Collections.emptyList());

    List<Item> actualItems = itemService.getItems();

    assertNotNull(actualItems);
    assertEquals(0, actualItems.size());
  }

  @Test
  void getItemById_shouldReturnItem_whenItemExistsAndBelongsToUser() {
    Item expectedItem = new Item();

    when(itemRepository.findByIdAndUserAndDeletedAtIsNull(1L, testUser))
        .thenReturn(Optional.of(expectedItem));

    Optional<Item> actualItem = itemService.getItemById(1L);

    assertTrue(actualItem.isPresent());
    assertEquals(expectedItem, actualItem.get());
  }

  @Test
  void getItemById_shouldReturnEmptyOptional_whenItemNotFound() {
    when(itemRepository.findByIdAndUserAndDeletedAtIsNull(1L, testUser))
        .thenReturn(Optional.empty());

    Optional<Item> actualItem = itemService.getItemById(1L);

    assertTrue(actualItem.isEmpty());
  }

  @Test
  void createItem_shouldSaveAndReturnItem_whenDataIsValid() {
    CreateItemDto dto = new CreateItemDto("New Item", "New description", "New brand", "M", "New");
    // Used to watch method call and capture passed argument (Item)
    ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);

    itemService.createItem(dto);

    verify(itemRepository).save(itemCaptor.capture());

    Item savedItem = itemCaptor.getValue();
    assertNotNull(savedItem);
    assertEquals("New Item", savedItem.getName());
    assertEquals(testUser, savedItem.getUser());
  }

  @Test
  void updateItem_shouldUpdateAndReturnItem_whenItemExists() {
    Item existingItem = new Item(testUser, "Old Name", "Old description", "Old brand", "M", "Old");
    UpdateItemDto dto = new UpdateItemDto("Updated Name", null, "Updated brand", null, null);

    when(itemRepository.findByIdAndUserAndDeletedAtIsNull(1L, testUser))
        .thenReturn(Optional.of(existingItem));
    when(itemRepository.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Item updatedItem = itemService.updateItem(1L, dto);

    assertNotNull(updatedItem);
    assertEquals("Updated Name", updatedItem.getName()); // If name was updated
    assertEquals("Old description", updatedItem.getDescription()); // If desc was NOT updated
    assertEquals("Updated brand", updatedItem.getBrand()); // If brand was updated
  }

  @Test
  void updateItem_shouldThrowItemNotFoundException_whenItemNotFound() {
    UpdateItemDto dto = new UpdateItemDto("Updated Name", null, null, null, null);

    when(itemRepository.findByIdAndUserAndDeletedAtIsNull(1L, testUser))
        .thenReturn(Optional.empty());

    assertThrows(ItemNotFoundException.class, () -> {
      itemService.updateItem(1L, dto);
    });
  }

  @Test
  void deleteItem_shouldSetDeletedAtTimestamp_whenItemExists() {
    Item existingItem = new Item();
    assertNull(existingItem.getDeletedAt()); // Ensures it's null (item not deleted)

    when(itemRepository.findByIdAndUserAndDeletedAtIsNull(1L, testUser))
        .thenReturn(Optional.of(existingItem));
    ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);

    itemService.deleteItem(1L);

    verify(itemRepository).save(itemCaptor.capture());
    Item deletedItem = itemCaptor.getValue();
    assertNotNull(deletedItem.getDeletedAt());
  }

  @Test
  void deleteItem_shouldThrowItemNotFoundException_whenItemNotFound() {
    when(itemRepository.findByIdAndUserAndDeletedAtIsNull(1L, testUser))
        .thenReturn(Optional.empty());

    assertThrows(ItemNotFoundException.class, () -> {
      itemService.deleteItem(1L);
    });
  }
}
