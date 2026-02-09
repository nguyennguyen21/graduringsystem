package com.devn.studentslearn.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CodeSubmissionRequest {

    @NotBlank(message = "Submitted code is required")
    private String submittedCode;

    @NotNull(message = "LanguageId is required")
    private Integer languageId;

    @NotBlank(message = "createdById is required")
    private String createdById;

    // Getters
    public String getSubmittedCode() { return submittedCode; }
    public Integer getLanguageId() { return languageId; }
    public String getCreatedById() { return createdById; }
}