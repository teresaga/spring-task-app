package com.teresadev.spring_task_app.rest;

import com.teresadev.spring_task_app.dto.TaskRequestDTO;
import com.teresadev.spring_task_app.dto.TaskResponseDTO;
import com.teresadev.spring_task_app.entity.Task;
import com.teresadev.spring_task_app.entity.User;
import com.teresadev.spring_task_app.repository.UserRepository;
import com.teresadev.spring_task_app.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<String> newTask(@RequestBody TaskRequestDTO task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Task savedTask = taskService.save(task, user);

            return ResponseEntity.ok("The task was successfully created.");
        }
        return null;
    }

    @GetMapping("")
    public List<TaskResponseDTO> getTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<TaskResponseDTO> tasks = new ArrayList<>();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            System.out.println(user.toString());
            tasks = taskService.findByUser(user);
        }

        return tasks;
    }
}
