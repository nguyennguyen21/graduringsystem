package com.devn.studentslearn.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class CreateFillInBlankQuestionRequestDTO extends BaseQuestionRequest {

    @NotNull(message = "Correct answer is required")
    private List<String> correctAnswer;

    @NotNull(message = "Tolerance is required")
    private Double tolerance;

    private Map<String, List<String>> synonyms;

    // Constructor mặc định (bắt buộc cho Jackson)
    public CreateFillInBlankQuestionRequestDTO() {}

    // Getter và Setter — PHẢI KHỚP KIỂU DỮ LIỆU VỚI JSON ĐƯỢC GỬI

    public List<String> getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * ⚠️ Quan trọng: Setter PHẢI nhận List<String>, KHÔNG PHẢI String
     * Nếu nhận String → Jackson không deserialize được mảng JSON → LỖI
     */
    public void setCorrectAnswer(List<String> correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Double getTolerance() {
        return tolerance;
    }

    public void setTolerance(Double tolerance) {
        this.tolerance = tolerance;
    }

    public Map<String, List<String>> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Map<String, List<String>> synonyms) {
        this.synonyms = synonyms;
    }
}