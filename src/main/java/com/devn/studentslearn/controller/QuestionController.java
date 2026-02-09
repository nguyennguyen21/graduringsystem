// src/main/java/com/devn/studentslearn/controller/QuestionController.java

package com.devn.studentslearn.controller;

import com.devn.studentslearn.dto.reponse.CreateCodeQuestionReponseDTO;
import com.devn.studentslearn.dto.request.BaseQuestionRequest;
import com.devn.studentslearn.model.Question;
import com.devn.studentslearn.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/{questionId}/full")
    public ResponseEntity<?> getFullQuestionDetails(@PathVariable String questionId) {
        // Sử dụng Object hoặc Wildcard <?> để hỗ trợ đa hình (cả MCQ và Code)
        Object response = questionService.getFullTaskDetails(questionId);
        return ResponseEntity.ok(response);
    }
    @PostMapping
    public ResponseEntity<Question> createQuestion(@RequestBody BaseQuestionRequest request) {
        Question createdQuestion = questionService.createQuestion(request);
        return ResponseEntity.ok(createdQuestion);
    }

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();
        return ResponseEntity.ok(questions);
    }


    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable String questionId) {
        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateQuestion(@RequestBody Question question) {
        questionService.updateQuestion(question);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{questionId}")
    public ResponseEntity<Question> getQuestion(@PathVariable String questionId) {
        Optional<Question> question = questionService.getQuestionById(questionId);
        return question.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}