package com.galibi.resellerledger.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record UserRegistrationDto(@Email @NotBlank String email, @NotBlank String password) {
}
