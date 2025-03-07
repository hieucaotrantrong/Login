package com.example.restcontroller;

import com.example.model.User; // Đúng package
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestUserController {

    private final UserService userService;

    @Autowired
    public RestUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/users")
    public List<User> users() {
        return userService.getAllUsers();
    }

    @PostMapping("/api/users")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }
}