package com.teresadev.spring_task_app.service;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.teresadev.spring_task_app.dto.AuthRequestDTO;
import com.teresadev.spring_task_app.dto.AuthResponseDTO;
import com.teresadev.spring_task_app.dto.RegisterRequest;
import com.teresadev.spring_task_app.entity.User;
import com.teresadev.spring_task_app.repository.UserRepository;
import com.teresadev.spring_task_app.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO registerUser(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .registrationDate(new Date())
                .build();

        userRepository.save(user);
        var jwtToken = jwtTokenUtil.generateToken(user);
        return AuthResponseDTO.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .token(jwtToken)
                .build();
    }

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtTokenUtil.generateToken(user);
        return AuthResponseDTO.builder()
                .email(request.getEmail())
                .token(jwtToken)
                .build();
    }
}
