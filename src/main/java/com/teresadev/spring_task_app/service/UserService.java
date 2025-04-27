package com.teresadev.spring_task_app.service;

import com.teresadev.spring_task_app.entity.User;

import java.util.Optional;

public interface UserService {

    User registerUser(User user);
    Optional<User> getUserByEmail(String email);
}
