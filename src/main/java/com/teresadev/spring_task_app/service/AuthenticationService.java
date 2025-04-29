package com.teresadev.spring_task_app.service;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.teresadev.spring_task_app.dto.AuthRequestDTO;
import com.teresadev.spring_task_app.dto.AuthResponseDTO;
import com.teresadev.spring_task_app.dto.RegisterRequest;
import com.teresadev.spring_task_app.entity.User;
import com.teresadev.spring_task_app.exception.CustomAuthException;
import com.teresadev.spring_task_app.repository.UserRepository;
import com.teresadev.spring_task_app.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true)
                .registrationDate(new Date())
                .build();

        userRepository.save(user);
        var jwtToken = jwtTokenUtil.generateToken(user);
        return AuthResponseDTO.builder()
                .email(request.getEmail())
                .token(jwtToken)
                .build();
    }

    public AuthResponseDTO authenticate(AuthRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new CustomAuthException("Credenciales incorrectas");
        } catch (UsernameNotFoundException e) {
            throw new CustomAuthException("Usuario no encontrado");
        } catch (Exception e) {
            throw new CustomAuthException("Error de autenticación");
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomAuthException("Usuario no encontrado"));

        var jwtToken = jwtTokenUtil.generateToken(user);

        return AuthResponseDTO.builder()
                .email(request.getEmail())
                .token(jwtToken)
                .build();
    }
}
