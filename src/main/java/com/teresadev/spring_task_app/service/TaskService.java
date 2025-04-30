package com.teresadev.spring_task_app.service;

import com.teresadev.spring_task_app.dto.TaskRequestDTO;
import com.teresadev.spring_task_app.dto.TaskResponseDTO;
import com.teresadev.spring_task_app.entity.User;
import com.teresadev.spring_task_app.repository.TaskRepository;
import com.teresadev.spring_task_app.entity.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private void authorize(User user, Task task) {
        if (!user.equals(task.getUser())) {
            throw new AccessDeniedException("You are not authorized to access this task");
        }
    }

    public Optional<Task> findById(Integer taskId) {
        return taskRepository.findById(taskId);
    }

    public List<TaskResponseDTO> findByUser(User userId) {
        List<Task> tasks = taskRepository.findByUser(userId);

        return tasks.stream()
                .map(task -> new TaskResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.isDone(),
                        task.getCreateAt(),
                        task.getCompletedAt(),
                        task.getStartDate(),
                        task.getEndDate()))
                .collect(Collectors.toList());
    }

    public TaskResponseDTO getByIdWithAuth(int id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        authorize(user, task);

        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .completedAt(task.getCompletedAt())
                .isDone(task.isDone())
                .build();
    }

    public void updateTask(int id, TaskRequestDTO request, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        authorize(user, task);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());

        taskRepository.save(task);
    }

    public Task save(TaskRequestDTO request, User user) {
        var task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createAt(new Date())
                .isDone(false)
                .user(user)
                .build();
        return taskRepository.save(task);
    }

    public void changeStatus(int id, User user) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));
        authorize(user, task);

        boolean current = task.isDone();
        task.setDone(!current);
        task.setCompletedAt(current ? null : new Date());

        taskRepository.save(task);
    }

    public void delete(int taskId, User user) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NoSuchElementException("Task not found"));

        authorize(user, task);
        taskRepository.deleteById(taskId);
    }
}
