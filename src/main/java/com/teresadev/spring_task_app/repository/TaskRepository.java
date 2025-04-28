package com.teresadev.spring_task_app.repository;

import com.teresadev.spring_task_app.entity.Task;
import com.teresadev.spring_task_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findById(int taskId);
    List<Task> findByUser(User userId);
}
