package com.devn.studentslearn.dto.reponse;

import lombok.Data;

@Data
public class QuestionResponse {
    private String questionId;
    private String problemStatement;
    private String initialCode;
    private String hiddenCode;
    private Integer timeLimit;
    private Integer memoryLimit;
}