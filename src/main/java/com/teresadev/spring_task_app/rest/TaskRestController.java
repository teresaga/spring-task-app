package com.teresadev.spring_task_app.rest;

import com.teresadev.spring_task_app.entity.Task;
import com.teresadev.spring_task_app.entity.User;
import com.teresadev.spring_task_app.repository.UserRepository;
import com.teresadev.spring_task_app.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    @GetMapping("")
    public List<Task> getTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Task> tasks = new ArrayList<>();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            System.out.println(user.toString());
            tasks = taskService.findByUser(user);
        }

        return tasks;
    }
}
