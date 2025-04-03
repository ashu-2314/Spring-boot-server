package com.example.springboot.controller;

import com.example.springboot.model.Quiz;
import com.example.springboot.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')") // 🚀 Only allow users with ADMIN role
public class AdminController {

    @Autowired
    private QuizRepository quizRepository;

    // 📝 Fetch All Quizzes (For Admin View)
    @GetMapping("/quizzes")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        return ResponseEntity.ok(quizzes);
    }

    // 📝 Create a New Quiz
    @PostMapping("/quizzes")
    public ResponseEntity<Quiz> createQuiz(@RequestBody Quiz quiz) {
        Quiz savedQuiz = quizRepository.save(quiz);
        return ResponseEntity.ok(savedQuiz);
    }

    // 📝 Update an Existing Quiz
    @PutMapping("/quizzes/{id}")
    public ResponseEntity<Quiz> updateQuiz(@PathVariable String id, @RequestBody Quiz updatedQuiz) {
        Optional<Quiz> quizOptional = quizRepository.findById(id);
        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            quiz.setTitle(updatedQuiz.getTitle());
            quiz.setDescription(updatedQuiz.getDescription());
            quiz.setQuestions(updatedQuiz.getQuestions());

            Quiz savedQuiz = quizRepository.save(quiz);
            return ResponseEntity.ok(savedQuiz);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 📝 Delete a Quiz
    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<String> deleteQuiz(@PathVariable String id) {
        if (quizRepository.existsById(id)) {
            quizRepository.deleteById(id);
            return ResponseEntity.ok("Quiz deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
