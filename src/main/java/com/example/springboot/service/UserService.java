package com.example.springboot.service;

import com.example.springboot.exception.UserNotFoundException;
import com.example.springboot.model.User;
import com.example.springboot.repository.UserRepository;
import com.example.springboot.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 1️⃣ **Register a New User** (Hashes password before saving)
    public User saveUser(User user) {
        // Hash the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // 2️⃣ **Login & Generate JWT**
    public String loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("Invalid credentials"));

        // Check if password matches
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserNotFoundException("Invalid credentials");
        }

        // Generate JWT
        return jwtUtil.generateToken(user.getUsername());
    }

    // 3️⃣ **Get User from Token**
    public User getUserFromToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new UserNotFoundException("No authentication token found.");
        }

        // Extract and validate username from token
        Optional<String> extractedUsername = jwtUtil.extractUsername(token);
        if (extractedUsername.isEmpty()) {
            throw new UserNotFoundException("Invalid or expired token.");
        }

        // Fetch user from the database
        User user = userRepository.findByUsername(extractedUsername.get())
                .orElseThrow(() -> new UserNotFoundException("User not found for token."));

        // Revalidate token against extracted email
        if (!jwtUtil.validateToken(token, user.getEmail())) {
            throw new UserNotFoundException("Invalid token for this user.");
        }

        return user;
    }

    // 4️⃣ **Update User Profile**
    public User updateUser(String id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
        user.setDob(updatedUser.getDob());
        user.setGender(updatedUser.getGender());
        user.setPreferences(updatedUser.getPreferences());

        return userRepository.save(user);
    }

    // 5️⃣ **Delete User**
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    public User registerUser(User user) {
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    
        // Save the user in MongoDB
        return userRepository.save(user);
    }
    
}
