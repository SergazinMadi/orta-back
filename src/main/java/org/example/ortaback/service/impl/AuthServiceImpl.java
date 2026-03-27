package org.example.ortaback.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ortaback.dto.request.LoginRequestDTO;
import org.example.ortaback.dto.request.UserCreateRequestDto;
import org.example.ortaback.dto.response.JwtResponseDto;
import org.example.ortaback.exception.InvalidCredentialsException;
import org.example.ortaback.exception.InvalidTokenException;
import org.example.ortaback.exception.ResourceAlreadyExistsException;
import org.example.ortaback.exception.ResourceNotFoundException;
import org.example.ortaback.persistence.models.User;
import org.example.ortaback.persistence.models.enums.UserRole;
import org.example.ortaback.persistence.repository.UserRepository;
import org.example.ortaback.service.AuthService;
import org.example.ortaback.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Transactional
    public String signUp(UserCreateRequestDto userCreateRequestDto) {
        log.info("Attempting to register user: {}", userCreateRequestDto.getUsername());

        if (userRepository.existsByUserName(userCreateRequestDto.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already taken: " + userCreateRequestDto.getUsername());
        }

        if (userRepository.existsByEmail(userCreateRequestDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already in use: " + userCreateRequestDto.getEmail());
        }

        User user = User.builder()
                .userName(userCreateRequestDto.getUsername())
                .fullName(userCreateRequestDto.getFullName())
                .email(userCreateRequestDto.getEmail())
                .password(passwordEncoder.encode(userCreateRequestDto.getPassword()))
                .phone(userCreateRequestDto.getPhone())
                .role(UserRole.USER)
                .emailVerified(false)
                .phoneVerified(false)
                .build();

        userRepository.save(user);
        log.info("User registered successfully: {}", user.getUsername());

        return "User registered successfully!";
    }

    public JwtResponseDto signIn(LoginRequestDTO loginRequest) {
        log.info("Attempting to sign in user: {}", loginRequest.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            User user = userRepository.findByUserName(userDetails.getUsername())
                    .or(() -> userRepository.findByEmail(userDetails.getUsername()))
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            log.info("User signed in successfully: {}", user.getUsername());

            return JwtResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .userId(user.getId())
                    .username(user.getUsername())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();

        } catch (BadCredentialsException e) {
            log.warn("Failed sign in attempt for user: {}", loginRequest.getUsername());
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    public JwtResponseDto refreshToken(String refreshToken) {
        log.info("Attempting to refresh token");

        try {
            String username = jwtUtil.getUsernameFromToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!jwtUtil.validateToken(refreshToken, userDetails)) {
                throw new InvalidTokenException("Invalid or expired refresh token");
            }

            String newAccessToken = jwtUtil.generateAccessToken(userDetails);
            String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

            User user = userRepository.findByUserName(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            log.info("Token refreshed successfully for user: {}", username);

            return JwtResponseDto.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .userId(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .role(user.getRole().name())
                    .build();

        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            throw new InvalidTokenException("Invalid or expired refresh token");
        }
    }
}
