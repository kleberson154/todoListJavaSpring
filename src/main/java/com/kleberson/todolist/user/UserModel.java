package com.kleberson.todolist.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tb_users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true)
    private String name;
    private String email;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
