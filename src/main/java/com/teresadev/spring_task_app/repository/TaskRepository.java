package com.teresadev.spring_task_app.repository;

import com.teresadev.spring_task_app.entity.Task;
import com.teresadev.spring_task_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUser(User userId);
}
