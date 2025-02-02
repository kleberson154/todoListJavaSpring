package com.kleberson.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody UserModel userModel){
        UserModel name = this.userRepository.findByName(userModel.getName());
        if (name != null){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }

        String password = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(password);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.userRepository.save(userModel));
    }
}
