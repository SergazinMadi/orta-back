package org.example.ortaback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ortaback.dto.response.JwtResponseDto;
import org.example.ortaback.dto.request.LoginRequestDTO;
import org.example.ortaback.dto.request.UserCreateRequestDto;
import org.example.ortaback.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        String result = authService.signUp(userCreateRequestDto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        JwtResponseDto response = authService.signIn(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDto> refreshToken(@RequestParam String refreshToken) {
        JwtResponseDto response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}
