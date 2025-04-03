package com.example.springboot.controller;

import com.example.springboot.model.User;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // 📝 User Registration Endpoint
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken.");
        }

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role if not provided
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("STUDENT"); // Default role is STUDENT
        }

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // 📝 User Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid username or password.");
        }

        User user = userOptional.get();

        // Validate password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid username or password.");
        }

        // Generate JWT Token using username
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok().body("{ \"token\": \"" + token + "\", \"role\": \"" + user.getRole() + "\" }");
    }


    // 📝 Auth Check Endpoint
@GetMapping("/check-auth")
public ResponseEntity<?> checkAuth(@RequestHeader("Authorization") String token) {
    if (token == null || !token.startsWith("Bearer ")) {
        return ResponseEntity.status(401).body("Unauthorized");
    }

    token = token.substring(7); // Remove "Bearer " prefix
    Optional<String> usernameOpt = jwtUtil.extractUsername(token);
    Optional<String> roleOpt = jwtUtil.extractRole(token);

    // ✅ Ensure values exist before using them
    if (usernameOpt.isEmpty() || roleOpt.isEmpty()) {
        return ResponseEntity.status(401).body("Invalid token.");
    }

    String username = usernameOpt.get();
    String role = roleOpt.get();

    Optional<User> user = userRepository.findByUsername(username);
    if (user.isEmpty()) {
        return ResponseEntity.status(401).body("Invalid token.");
    }

    return ResponseEntity.ok().body("{ \"username\": \"" + username + "\", \"role\": \"" + role + "\" }");
}

}
