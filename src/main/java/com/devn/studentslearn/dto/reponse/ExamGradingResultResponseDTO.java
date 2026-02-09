package com.devn.studentslearn.dto.reponse;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ExamGradingResultResponseDTO {

    private String examResultId;
    private int totalQuestions;
    private int correctAnswers;
    private float totalScore;
    private float bloomScore;
    private Instant examDate;

    // Constructors
    public ExamGradingResultResponseDTO() {}

    public ExamGradingResultResponseDTO(
            String examResultId,
            int totalQuestions,
            int correctAnswers,
            float totalScore,
            float bloomScore,
            Instant examDate
    ) {
        this.examResultId = examResultId;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.totalScore = totalScore;
        this.bloomScore = bloomScore;
        this.examDate = examDate;
    }
}