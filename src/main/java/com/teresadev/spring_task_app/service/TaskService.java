package com.teresadev.spring_task_app.service;

import com.teresadev.spring_task_app.dto.TaskRequestDTO;
import com.teresadev.spring_task_app.entity.Task;
import com.teresadev.spring_task_app.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    List<Task> findAll();
    List<Task> findByUser(User userId);
    Task save(TaskRequestDTO task, User user);
}
