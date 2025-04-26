package com.teresadev.spring_task_app.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskRestController {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World!";
    }
}
