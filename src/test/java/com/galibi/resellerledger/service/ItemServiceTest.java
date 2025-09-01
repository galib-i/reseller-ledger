package com.galibi.resellerledger.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.galibi.resellerledger.entities.Item;
import com.galibi.resellerledger.entities.User;
import com.galibi.resellerledger.repositories.ItemRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

// TODO: remove
// methodName_shouldDoSomething_whenCondition
// Arrange, Act, Assert

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
}
