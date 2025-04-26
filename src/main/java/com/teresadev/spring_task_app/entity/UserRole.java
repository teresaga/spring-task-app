package com.teresadev.spring_task_app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="user-roles")
@Data
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userRoleId;

    @Column(name = "user_role_name")
    private String userRoleName;

    @OneToMany(mappedBy = "userRoleId", cascade = CascadeType.ALL)
    public List<User> users;
}
