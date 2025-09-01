package com.galibi.resellerledger.controllers;

import com.galibi.resellerledger.dto.UserRegistrationDto;
import com.galibi.resellerledger.entities.User;
import com.galibi.resellerledger.repositories.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @PostMapping
  public User createUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
    User newUser = new User();
    newUser.setEmail(registrationDto.email());
    newUser.setPasswordHash(passwordEncoder.encode(registrationDto.password()));

    return userRepository.save(newUser);
  }
}
