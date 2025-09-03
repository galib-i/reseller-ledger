package com.galibi.resellerledger.service;

import com.galibi.resellerledger.dto.LogTransactionDto;
import com.galibi.resellerledger.entities.Item;
import com.galibi.resellerledger.entities.Platform;
import com.galibi.resellerledger.entities.Transaction;
import com.galibi.resellerledger.entities.User;
import com.galibi.resellerledger.repositories.ItemRepository;
import com.galibi.resellerledger.repositories.PlatformRepository;
import com.galibi.resellerledger.repositories.TransactionRepository;
import com.galibi.resellerledger.service.exception.ItemNotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final ItemRepository itemRepository;
  private final PlatformRepository platformRepository;

  public TransactionService(TransactionRepository transactionRepository,
      ItemRepository itemRepository, PlatformRepository platformRepository) {
    this.transactionRepository = transactionRepository;
    this.itemRepository = itemRepository;
    this.platformRepository = platformRepository;
  }

  public Transaction logTransaction(LogTransactionDto transactionDto) {
    User currentUser = getCurrentUser();

    Item item =
        itemRepository.findByIdAndUserAndDeletedAtIsNull(transactionDto.itemId(), currentUser)
            .orElseThrow(() -> new ItemNotFoundException(
                "No active item found with id: " + transactionDto.itemId()));

    Transaction newTransaction = new Transaction(item, currentUser, transactionDto.type(),
        transactionDto.value(), transactionDto.transactionDate());

    newTransaction.setPlatformFee(transactionDto.platformFee());
    newTransaction.setDeliveryFee(transactionDto.deliveryFee());

    if (transactionDto.platformId() != null) {
      Platform platform = platformRepository.findById(transactionDto.platformId()).orElse(null);
      newTransaction.setPlatform(platform);
    }

    return transactionRepository.save(newTransaction);
  }

  private User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (User) authentication.getPrincipal();
  }
}
