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

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> findTaskById(@PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Task existingTask = taskService.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Task not found"));

            if (!user.equals(existingTask.getUser())) {
                System.out.println("###ACCESS DENIED: This user is not authorized to access this task");
                throw new AccessDeniedException("You are not authorized to access this task");
            }

            TaskResponseDTO result = TaskResponseDTO.builder()
                            .id(existingTask.getId())
                            .title(existingTask.getTitle())
                            .description(existingTask.getDescription())
                            .startDate(existingTask.getStartDate())
                            .endDate(existingTask.getEndDate())
                            .completedAt(existingTask.getCompletedAt())
                            .isDone(existingTask.isDone())
                            .build();

            return ResponseEntity.ok(result);
        }
        return null;
    }

    @PostMapping("")
    public ResponseEntity<String> newTask(@Valid @RequestBody TaskRequestDTO task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Task savedTask = taskService.save(task, user);

            return ResponseEntity.ok("The task was successfully created.");
        }
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTask(@PathVariable("id") int id, @Valid @RequestBody TaskRequestDTO taskRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Task existingTask = taskService.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Task not found"));

            if (!user.equals(existingTask.getUser())) {
                System.out.println("###ACCESS DENIED: This user is not authorized to edit this task");
                throw new AccessDeniedException("You are not authorized to edit this task");
            }

            existingTask.setTitle(taskRequest.getTitle());
            existingTask.setDescription(taskRequest.getDescription());
            existingTask.setStartDate(taskRequest.getStartDate());
            existingTask.setEndDate(taskRequest.getEndDate());

            taskService.update(existingTask);

            return ResponseEntity.ok("The task was successfully changed.");
        }
        return null;
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<String> changeStatus(@PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Task existingTask = taskService.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Task not found"));

            if (!user.equals(existingTask.getUser())) {
                System.out.println("###ACCESS DENIED: This user is not authorized to edit this task");
                throw new AccessDeniedException("You are not authorized to edit this task");
            }

            boolean currentValue = existingTask.isDone();

            if (!currentValue) {
                existingTask.setCompletedAt(new Date());
            } else {
                existingTask.setCompletedAt(null);
            }

            existingTask.setDone(!currentValue);
            taskService.update(existingTask);

            return ResponseEntity.ok("The task status was successfully changed.");
        }
        return null;
    }

    @GetMapping("")
    public List<TaskResponseDTO> getTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<TaskResponseDTO> tasks = new ArrayList<>();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            tasks = taskService.findByUser(user);
        }

        return tasks;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Task existingTask = taskService.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Task not found"));

            if (!user.equals(existingTask.getUser())) {
                System.out.println("###ACCESS DENIED: This user is not authorized to delete this task");
                throw new AccessDeniedException("You are not authorized to delete this task");
            }

            taskService.delete(existingTask.getId());

            return ResponseEntity.ok("The task was removed.");
        }
        return null;
    }
}
