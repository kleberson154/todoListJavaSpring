package com.kleberson.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        taskModel.setUserId((UUID) request.getAttribute("userId"));
        LocalDateTime date = LocalDateTime.now();
        if (date.isAfter(taskModel.getStartAt()) || date.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task start date or end date cannot be before current date");
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task start date cannot be before end date");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.taskRepository.save(taskModel));
    }

    @GetMapping("/")
    public List<TaskModel> list (HttpServletRequest request){
        try {
            UUID idUser = (UUID) request.getAttribute("userId");
            if (idUser == null) {
                throw new IllegalArgumentException("userId não encontrado no request");
            }
            return this.taskRepository.findByUserId(idUser);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao obter a lista de tarefas", e);
        }
    }
}
