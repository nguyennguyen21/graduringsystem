package com.devn.studentslearn.dto.reponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CreateFillInBlankQuestionResponseDTO {

    @NotBlank
    private String content;

    @NotBlank
    private String bloomLevel;

    @NotNull
    private Integer chapterId;

    @NotBlank
    private String createdById;

    private String img;

    @NotNull
    private List<String> correctAnswer;

    @NotNull
    private List<Double> tolerance;

    private Map<String, List<String>> synonyms;

    private Instant createdAt;
    private Instant updatedAt;

    // Constructor — ĐÃ SỬA ĐÚNG KIỂU synonyms
    public CreateFillInBlankQuestionResponseDTO(
            String content,
            String bloomLevel,
            Integer chapterId,
            String createdById,
            String img,
            String correctAnswer,
            Double tolerance,
            Map<String, List<String>> synonyms,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.content = content;
        this.bloomLevel = bloomLevel;
        this.chapterId = chapterId;
        this.createdById = createdById;
        this.img = img;
        this.correctAnswer = Collections.singletonList(correctAnswer);
        this.tolerance = Collections.singletonList(tolerance);
        this.synonyms = synonyms;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBloomLevel() {
        return bloomLevel;
    }

    public void setBloomLevel(String bloomLevel) {
        this.bloomLevel = bloomLevel;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public @NotNull List<String> getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = Collections.singletonList(correctAnswer);
    }

    public @NotNull List<Double> getTolerance() {
        return tolerance;
    }

    public void setTolerance(Double tolerance) {
        this.tolerance = Collections.singletonList(tolerance);
    }

    public Map<String, List<String>> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Map<String, List<String>> synonyms) {
        this.synonyms = synonyms;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}