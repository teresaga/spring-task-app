package com.teresadev.spring_task_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CustomAuthException extends RuntimeException{
    public CustomAuthException(String message) {
        super(message);
    }
}
