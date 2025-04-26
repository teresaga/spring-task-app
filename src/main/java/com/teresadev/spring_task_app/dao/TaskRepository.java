package com.teresadev.spring_task_app.dao;

import com.teresadev.spring_task_app.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Integer> {
}
