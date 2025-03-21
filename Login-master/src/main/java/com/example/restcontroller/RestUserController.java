package com.example.restcontroller;

import com.example.model.User;
import com.example.model.AuthRequest;
import com.example.service.UserService;
import com.example.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class RestUserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public RestUserController(UserService userService, JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // Lấy danh sách tất cả người dùng
    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with id: " + id);
        }
    }

    // Tạo người dùng mới (Đăng ký)
    @PostMapping("/register")
    public String registerUser(@RequestBody AuthRequest userRequest) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userRequest.getPassword());

        User user = new User();
        user.setEmail(userRequest.getUsername()); // Đổi username thành email
        user.setPassword(encodedPassword);

        userService.saveUser(user);
        return "Đăng ký thành công!";
    }

    // Đăng nhập và lấy token
    @PostMapping("/login")
    public String loginUser(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(),
                authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Sai tên đăng nhập hoặc mật khẩu!");
        }
    }

}

// Cập nhật thông tin người dùng
// @PutMapping("/{id}")
// public User updateUser(@PathVariable Long id, @RequestBody User userDetails)
// {
// return userService.updateUser(id, userDetails);
// }

// // Xóa người dùng theo ID
// @DeleteMapping("/{id}")
// public String deleteUser(@PathVariable Long id) {
// return userService.deleteUser(id);
// }
