package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class UserController {

    @GetMapping("/user/home")
    public String userHome(Model model, Principal principal) {
        String username = principal.getName();
        model.addAttribute("username", username);
        return "user-home";
    }
}
