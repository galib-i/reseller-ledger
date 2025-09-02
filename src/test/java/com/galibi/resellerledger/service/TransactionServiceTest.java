package com.galibi.resellerledger.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.galibi.resellerledger.dto.LogTransactionDto;
import com.galibi.resellerledger.entities.Item;
import com.galibi.resellerledger.entities.Transaction;
import com.galibi.resellerledger.entities.User;
import com.galibi.resellerledger.repositories.ItemRepository;
import com.galibi.resellerledger.repositories.TransactionRepository;
import com.galibi.resellerledger.service.exception.ItemNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

  @Mock
  private ItemRepository itemRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private TransactionService transactionService;

  private User testUser;

  @BeforeEach
  void setUp() {
    testUser = new User();
    testUser.setEmail("user@test.com");
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);

    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(testUser);
    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  void logTransaction_shouldSaveTransaction_whenItemExistsAndBelongsToUser() {
    LogTransactionDto dto = createTestDto(1L);
    Item existingItem = new Item();
    existingItem.setUser(testUser);

    when(itemRepository.findByIdAndUserAndDeletedAtIsNull(1L, testUser))
        .thenReturn(Optional.of(existingItem));
    when(transactionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

    Transaction result = transactionService.logTransaction(dto);

    assertNotNull(result);
    assertEquals(new BigDecimal("10.00"), result.getValue());
    assertEquals("PURCHASE", result.getType());
    assertEquals(testUser, result.getUser());
    assertEquals(existingItem, result.getItem());
  }

  @Test
  void logTransaction_shouldThrowItemNotFoundException_whenItemDoesNotExist() {
    LogTransactionDto dto = createTestDto(99L);

    when(itemRepository.findByIdAndUserAndDeletedAtIsNull(99L, testUser))
        .thenReturn(Optional.empty());

    assertThrows(ItemNotFoundException.class, () -> {
      transactionService.logTransaction(dto);
    });
  }

  private LogTransactionDto createTestDto(Long itemId) {
    return new LogTransactionDto(itemId, null, "PURCHASE", new BigDecimal("10.00"), null, null,
        LocalDate.now());
  }
}
