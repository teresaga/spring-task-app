package com.teresadev.spring_task_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private int userId;
    private String username;
    private String email;
    private Boolean isActive;
    private Date registrationDate;
}
