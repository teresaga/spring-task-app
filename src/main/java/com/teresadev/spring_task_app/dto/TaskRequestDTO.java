package com.teresadev.spring_task_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
}
