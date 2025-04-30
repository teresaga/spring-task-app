package com.teresadev.spring_task_app.rest;

import com.teresadev.spring_task_app.dto.TaskRequestDTO;
import com.teresadev.spring_task_app.dto.TaskResponseDTO;
import com.teresadev.spring_task_app.entity.Task;
import com.teresadev.spring_task_app.entity.User;
import com.teresadev.spring_task_app.repository.UserRepository;
import com.teresadev.spring_task_app.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) throw new AccessDeniedException("Not authenticated");
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findTaskById(@PathVariable("id") int id) {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(taskService.getByIdWithAuth(id, user));
    }

    @PostMapping("")
    public ResponseEntity<String> newTask(@Valid @RequestBody TaskRequestDTO task) {
        User user = getAuthenticatedUser();
        taskService.save(task, user);
        return ResponseEntity.ok("The task was successfully created.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable("id") int id, @Valid @RequestBody TaskRequestDTO taskRequest) {
        User user = getAuthenticatedUser();
        taskService.updateTask(id, taskRequest, user);
        return ResponseEntity.ok("The task was successfully changed.");
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable("id") int id) {
        User user = getAuthenticatedUser();
        taskService.changeStatus(id, user);
        return ResponseEntity.ok("The task status was successfully changed.");
    }

    @GetMapping("")
    public ResponseEntity<List<TaskResponseDTO>> getTasks() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(taskService.findByUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") int id) {
        User user = getAuthenticatedUser();
        taskService.delete(id, user);
        return ResponseEntity.ok("The task was removed.");
    }
}
