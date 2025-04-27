package com.teresadev.spring_task_app.service;

import com.teresadev.spring_task_app.repository.UserRepository;
import com.teresadev.spring_task_app.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<User> result = Optional.ofNullable(userRepository.findByEmail(email));
        return result;
    }
}
