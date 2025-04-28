package com.teresadev.spring_task_app.service;

import com.teresadev.spring_task_app.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    List<Task> findAll();
}
