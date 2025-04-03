package com.example.springboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "quiz_attempts") // MongoDB Collection
public class QuizAttempt {

    @Id
    private String id;
    private String quizId;           // ID of the attempted quiz
    private String studentUsername;  // Username of the student attempting the quiz
    private int score;               // Score achieved by the student

    // ✅ Constructors
    public QuizAttempt() {}

    public QuizAttempt(String quizId, String studentUsername, int score) {
        this.quizId = quizId;
        this.studentUsername = studentUsername;
        this.score = score;
    }

    // ✅ Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getQuizId() { return quizId; }
    public void setQuizId(String quizId) { this.quizId = quizId; }

    public String getStudentUsername() { return studentUsername; }
    public void setStudentUsername(String studentUsername) { this.studentUsername = studentUsername; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}
