package com.example.springboot.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "quizzes")
public class Quiz {
    @Id
    private String id;
    private String title;
    private String description;
    private List<Question> questions;
    private String createdBy;

    // Constructors
    public Quiz() {}

    public Quiz(String title, String description, List<Question> questions, String createdBy) {
        this.title = title;
        this.description = description;
        this.questions = questions;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
