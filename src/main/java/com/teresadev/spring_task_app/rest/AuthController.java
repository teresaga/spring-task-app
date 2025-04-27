package com.teresadev.spring_task_app.rest;

import com.teresadev.spring_task_app.entity.User;
import com.teresadev.spring_task_app.security.JwtTokenUtil;
import com.teresadev.spring_task_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        Optional<User> userOptional = userService.getUserByEmail(user.getEmail());

        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User existed already");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        Optional<User> dbUser = userService.getUserByEmail(user.getEmail());
        if (dbUser.isPresent() && user.getPassword().equals(dbUser.get().getPassword())) {
            return ResponseEntity.ok(jwtTokenUtil.generateToken(dbUser.get().getEmail()));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Invalid credentials");
    }
}
