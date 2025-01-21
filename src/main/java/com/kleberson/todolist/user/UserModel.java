package com.kleberson.todolist.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserModel {
    private String name;
    private String email;
    private String password;
}
