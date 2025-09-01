package com.galibi.resellerledger.controllers;

import com.galibi.resellerledger.dto.LoginRequestDto;
import com.galibi.resellerledger.dto.LoginResponseDto;
import com.galibi.resellerledger.service.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;

  public AuthenticationController(AuthenticationManager authenticationManager,
      UserDetailsService userDetailsService, JwtService jwtService) {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public LoginResponseDto login(@RequestBody LoginRequestDto loginRequest) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));

    UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.email());
    String token = jwtService.generateToken(userDetails);

    return new LoginResponseDto(token);
  }
}
