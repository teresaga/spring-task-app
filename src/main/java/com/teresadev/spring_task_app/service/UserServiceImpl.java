package com.teresadev.spring_task_app.service;

import com.teresadev.spring_task_app.dto.UserResponseDTO;
import com.teresadev.spring_task_app.repository.UserRepository;
import com.teresadev.spring_task_app.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);
        user.setRegistrationDate(new Date());

        User savedUser = userRepository.save(user);

        return mapToUserResponseDTO(savedUser);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<User> result = Optional.ofNullable(userRepository.findByEmail(email));
        return result;
    }

    private UserResponseDTO mapToUserResponseDTO(User user) {
        return new UserResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getIsActive(),
                user.getRegistrationDate()
        );
    }
}
