package com.example.springboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String email;
    private String phone;
    private String dob;
    private String gender;
    private String username;
    private String password; // Hashed before storing
    private List<String> preferences;
    private boolean termsAccepted;
    private String role; // "STUDENT" or "ADMIN"
}
