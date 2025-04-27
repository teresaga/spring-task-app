package com.teresadev.spring_task_app.rest;

import com.teresadev.spring_task_app.dto.AuthRequestDTO;
import com.teresadev.spring_task_app.dto.AuthResponseDTO;
import com.teresadev.spring_task_app.dto.ErrorResponseDTO;
import com.teresadev.spring_task_app.dto.UserResponseDTO;
import com.teresadev.spring_task_app.entity.User;
import com.teresadev.spring_task_app.security.JwtTokenUtil;
import com.teresadev.spring_task_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint accessible");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        Optional<User> userOptional = userService.getUserByEmail(user.getEmail());

        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDTO("User already exists"));
        }

        UserResponseDTO savedUser = userService.registerUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequest) {
        try {
            // Authentication
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            // Configure Security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // get authenticated user
            User user = (User) authentication.getPrincipal();

            // create JWT
            String jwt = jwtTokenUtil.generateToken(user.getEmail());

            // Create response DTO
            AuthResponseDTO responseDTO = new AuthResponseDTO(jwt, user.getUsername(), user.getEmail());

            return ResponseEntity.ok(responseDTO);
        } catch (BadCredentialsException e) {
            // Handle invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponseDTO("Invalid email or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
