package org.example.ortaback.service;

import org.example.ortaback.dto.request.LoginRequestDTO;
import org.example.ortaback.dto.request.UserCreateRequestDto;
import org.example.ortaback.dto.response.JwtResponseDto;

public interface AuthService {
    String signUp(UserCreateRequestDto userCreateRequestDto);

    JwtResponseDto signIn(LoginRequestDTO loginRequest);

    JwtResponseDto refreshToken(String refreshToken);
}
