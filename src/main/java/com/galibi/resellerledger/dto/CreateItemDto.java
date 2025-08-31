package com.galibi.resellerledger.dto;

import jakarta.validation.constraints.NotBlank;


public record CreateItemDto(@NotBlank String name, String description, String brand, String size,
        String condition) {
}
