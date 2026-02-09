package com.devn.studentslearn.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "staticanalysisreports")
public class Staticanalysisreport {
    @Id
    @Size(max = 36)
    @Column(name = "Id", nullable = false, length = 36)
    private String id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "SubmissionId", nullable = false)
    private Codesubmission submission;

    @Size(max = 50)
    @ColumnDefault("'cppcheck'")
    @Column(name = "ToolName", length = 50)
    private String toolName;

    @NotNull
    @Lob
    @Column(name = "IssueSeverity", nullable = false)
    private String issueSeverity;

    @Column(name = "LineNumber")
    private Integer lineNumber;

    @NotNull
    @Lob
    @Column(name = "Message", nullable = false)
    private String message;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CreatedAt")
    private Instant createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Codesubmission getSubmission() {
        return submission;
    }

    public void setSubmission(Codesubmission submission) {
        this.submission = submission;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public String getIssueSeverity() {
        return issueSeverity;
    }

    public void setIssueSeverity(String issueSeverity) {
        this.issueSeverity = issueSeverity;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}