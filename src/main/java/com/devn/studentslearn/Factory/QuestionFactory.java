package com.devn.studentslearn.Factory;

import com.devn.studentslearn.strategy.implement.IQuestionStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuestionFactory {

    private final List<IQuestionStrategy> strategies;

    public QuestionFactory(List<IQuestionStrategy> strategies) {
        this.strategies = strategies;
    }

    public IQuestionStrategy getStrategy(String questionType) {
        if (questionType == null || questionType.isBlank()) {
            throw new IllegalArgumentException("questionType is required");
        }
        return strategies.stream()
                .filter(s -> s.supports(questionType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported question type: " + questionType));
    }
}