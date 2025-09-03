package com.galibi.resellerledger.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;


public record LogTransactionDto(@NotNull Long itemId, Long platformId, @NotBlank String type,
    @NotNull BigDecimal value, BigDecimal platformFee, BigDecimal deliveryFee,
    @NotNull @PastOrPresent LocalDate transactionDate) {
}
