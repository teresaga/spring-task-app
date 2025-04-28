package com.teresadev.spring_task_app.service;

import com.teresadev.spring_task_app.entity.User;
import com.teresadev.spring_task_app.repository.TaskRepository;
import com.teresadev.spring_task_app.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> findByUser(User userId) {
        return taskRepository.findByUser(userId);
    }
}
