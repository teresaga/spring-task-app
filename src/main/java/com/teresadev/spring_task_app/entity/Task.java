package com.teresadev.spring_task_app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
    @Column(name = "is_done")
    private boolean isDone;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "completed_at")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date completedAt;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "start_date")
    private Date startDate;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(name = "end_date")
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
