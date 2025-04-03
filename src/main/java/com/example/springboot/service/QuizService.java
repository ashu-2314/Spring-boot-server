package com.example.springboot.service;

import com.example.springboot.model.Quiz;
import com.example.springboot.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(String id) {
        return quizRepository.findById(id);
    }

    public Quiz createQuiz(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Quiz updateQuiz(String id, Quiz updatedQuiz) {
        if (quizRepository.existsById(id)) {
            updatedQuiz.setId(id);
            return quizRepository.save(updatedQuiz);
        }
        return null;
    }

    public void deleteQuiz(String id) {
        quizRepository.deleteById(id);
    }
}
