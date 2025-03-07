package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;

@Controller
public class AdminController {
    /*--------------------------------------
    * Admin Home
    /*--------------------------------------
    */
    @GetMapping("/admin")
    public String adminPage(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        return "admin";
    }
}
