package com.example.springboot.controller;

import com.example.springboot.model.Credentials;
import com.example.springboot.model.User;
import com.example.springboot.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Register a New User
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // ✅ Login User and Set JWT Cookie
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Credentials credentials,
                                                     HttpServletResponse response) {
        String token = userService.loginUser(credentials.getUsername(), credentials.getPassword());

        // Set JWT as HTTP-only, Secure Cookie
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false) // Set true for production with HTTPS
                .path("/")
                .maxAge(60 * 60) // 1 Hour
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return ResponseEntity.ok(Map.of("message", "Login successful", "username", credentials.getUsername()));
    }

    // ✅ Logout by Clearing Cookie
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        ResponseCookie jwtCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // Remove cookie
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    // ✅ Check Authentication from JWT Cookie
    @GetMapping("/check-auth")
    public ResponseEntity<Map<String, User>> checkAuth(@CookieValue(value = "jwt", required = false) String token) {
        User user = userService.getUserFromToken(token);
        return ResponseEntity.ok(Map.of("user", user));
    }

    // ✅ Update User Profile
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updatedUser) {
        User user = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    // ✅ Delete User
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
