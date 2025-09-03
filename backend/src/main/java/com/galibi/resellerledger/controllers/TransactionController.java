package com.galibi.resellerledger.controllers;

import com.galibi.resellerledger.dto.LogTransactionDto;
import com.galibi.resellerledger.entities.Transaction;
import com.galibi.resellerledger.service.TransactionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

  private final TransactionService transactionService;

  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping
  public ResponseEntity<Transaction> logTransaction(
      @Valid @RequestBody LogTransactionDto transactionDto) {
    Transaction savedTransaction = transactionService.logTransaction(transactionDto);

    return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
  }
}
