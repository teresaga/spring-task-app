package com.teresadev.spring_task_app.service;

import com.teresadev.spring_task_app.dto.TaskRequestDTO;
import com.teresadev.spring_task_app.dto.TaskResponseDTO;
import com.teresadev.spring_task_app.entity.User;
import com.teresadev.spring_task_app.repository.TaskRepository;
import com.teresadev.spring_task_app.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
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

    public Task save(TaskRequestDTO request, User user) {
        var task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .completedAt(new Date())
                .isDone(false)
                .user(user)
                .build();
        return taskRepository.save(task);
    }
}
