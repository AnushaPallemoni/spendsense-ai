package com.anusha.spendsense_backend.service;

import com.anusha.spendsense_backend.dto.AuthResponse;
import com.anusha.spendsense_backend.dto.LoginRequest;
import com.anusha.spendsense_backend.dto.RegisterRequest;
import com.anusha.spendsense_backend.model.User;
import com.anusha.spendsense_backend.repository.UserRepository;
import com.anusha.spendsense_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        }

        User user = User.builder()
                .name(request.name().trim())
                .email(email)
                .password(passwordEncoder.encode(request.password()))
                .build();
        userRepository.save(user);

        return new AuthResponse(jwtService.generateToken(email), email, user.getName());
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        return new AuthResponse(jwtService.generateToken(email), email, user.getName());
    }
}
