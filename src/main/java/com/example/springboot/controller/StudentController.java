package com.example.springboot.controller;

import com.example.springboot.model.Quiz;
import com.example.springboot.model.QuizAttempt;
import com.example.springboot.repository.QuizRepository;
import com.example.springboot.repository.QuizAttemptRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/student")
@PreAuthorize("hasAuthority('STUDENT')")
public class StudentController {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    // 📌 Fetch All Quizzes
    @GetMapping("/quizzes")
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findAll();
        return ResponseEntity.ok(quizzes);
    }

    // 🏆 Attempt a Quiz
    @PostMapping("/quizzes/{quizId}/attempt")
    public ResponseEntity<String> attemptQuiz(@PathVariable String quizId, Principal principal) {
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);

        if (quizOptional.isPresent()) {
            QuizAttempt attempt = new QuizAttempt(quizId, principal.getName(), 0); // Score 0 initially
            quizAttemptRepository.save(attempt);
            return ResponseEntity.ok("Quiz attempt started successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 📝 Submit Quiz Answers
    @PostMapping("/quizzes/{quizId}/submit")
    public ResponseEntity<String> submitQuiz(@PathVariable String quizId, @RequestBody List<Integer> answers, Principal principal) {
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);

        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            int score = calculateScore(quiz, answers);

            QuizAttempt attempt = new QuizAttempt(quizId, principal.getName(), score);
            quizAttemptRepository.save(attempt);

            return ResponseEntity.ok("Quiz submitted! Your score: " + score);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 📊 View Student Quiz History
    @GetMapping("/quiz-history")
    public ResponseEntity<List<QuizAttempt>> getStudentQuizHistory(Principal principal) {
        List<QuizAttempt> attempts = quizAttemptRepository.findByStudentUsername(principal.getName());
        return ResponseEntity.ok(attempts);
    }

    // ✅ Helper Method: Calculate Score
    private int calculateScore(Quiz quiz, List<Integer> answers) {
        int score = 0;
        for (int i = 0; i < quiz.getQuestions().size(); i++) {
            if (quiz.getQuestions().get(i) != null && answers.get(i) != null
                && quiz.getQuestions().get(i).getCorrectAnswerIndex() == answers.get(i)) {
                score++;
            }
        }
        return score;
    }
    
}
