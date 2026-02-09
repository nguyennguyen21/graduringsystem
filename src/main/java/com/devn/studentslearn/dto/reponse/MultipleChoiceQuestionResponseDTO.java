package com.devn.studentslearn.dto.reponse;

import java.time.Instant;
import java.util.List;

public class MultipleChoiceQuestionResponseDTO {
    private String id;
    private String content;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private List<String> correctOptions;
    private Instant createdAt;

    public MultipleChoiceQuestionResponseDTO() {}

    public MultipleChoiceQuestionResponseDTO(
            String id, String content,
            String optionA, String optionB, String optionC, String optionD,
            List<String> correctOptions,
            Instant createdAt
    ) {
        this.id = id;
        this.content = content;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOptions = correctOptions;
        this.createdAt = createdAt;
    }

    // Getters
    public String getId() { return id; }
    public String getContent() { return content; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public List<String> getCorrectOptions() { return correctOptions; }
    public Instant getCreatedAt() { return createdAt; }
}