// File: src/main/java/com/devn/studentslearn/strategy/implement/IQuestionStrategy.java
package com.devn.studentslearn.strategy.implement;

import com.devn.studentslearn.dto.request.BaseQuestionRequest;
import com.devn.studentslearn.model.Question;

import java.util.Optional;

public interface IQuestionStrategy {
    boolean supports(String questionType);
    Question addQuestion(BaseQuestionRequest request);
    void deleteQuestion(Question question);
    void updateQuestion(Question question);
    Optional<Question> getQuestionByQuestionId(String questionId);
}