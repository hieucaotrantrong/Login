package com.example.controller;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    
    /*--------------------------------------
    *  Login
    /*--------------------------------------
    */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /*--------------------------------------
    *  Register
    /*--------------------------------------
    */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    /*--------------------------------------
    *  Edit user and password
    /*--------------------------------------
    */
    @GetMapping("/home/edit")
    public String editUser(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản!"));
        model.addAttribute("user", user);
        return "edit";
    }

    /*--------------------------------------
    *  Redirect to Login after register
    /*--------------------------------------
    */
    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }

    /*--------------------------------------
    *  HomePage
    /*--------------------------------------
    */
    @GetMapping("/home")
    public String homePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản!"));
        model.addAttribute("user", user);
        return "home";
    }

    /*--------------------------------------
    *  Delete user
    /*--------------------------------------
    */
    @GetMapping("/home/delete")
    public String deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản!"));
        userRepository.delete(user);
        SecurityContextHolder.clearContext();
        return "redirect:/register";
    }

    /*--------------------------------------
    *  Update
    /*--------------------------------------
    */
    @PostMapping("/home/update")
    public String updateUser(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute User user) {
        User existingUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản!"));

        /*--------------------------------------
         * Update Email,Password
        /*--------------------------------------
         */
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(existingUser);

        /*--------------------------------------
         * Deleted Logined 
        /*--------------------------------------
         */
        SecurityContextHolder.clearContext();

        return "redirect:/login";
    }
}