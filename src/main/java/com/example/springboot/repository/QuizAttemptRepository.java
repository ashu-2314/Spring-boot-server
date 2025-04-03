package com.example.springboot.repository;

import com.example.springboot.model.QuizAttempt;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface QuizAttemptRepository extends MongoRepository<QuizAttempt, String> {
    List<QuizAttempt> findByStudentUsername(String studentUsername);
}
