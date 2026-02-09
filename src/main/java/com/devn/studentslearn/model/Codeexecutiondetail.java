package com.devn.studentslearn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "codeexecutiondetails")
public class Codeexecutiondetail {
    @Id
    @Column(name = "Id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "SubmissionId", nullable = false)
    private Codesubmission submission;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "TestCaseId", nullable = false)
    private Codetestcase testCase;

    @Lob
    @Column(name = "ActualOutput")
    private String actualOutput;

    @ColumnDefault("0")
    @Column(name = "IsCorrect")
    private Boolean isCorrect;

    @Column(name = "ExecutionTimeMs")
    private Integer executionTimeMs;

    @Column(name = "MemoryUsedKb")
    private Integer memoryUsedKb;

    @Lob
    @Column(name = "ErrorMessage")
    private String errorMessage;

    @Column(name = "ErrorLine")
    private Integer errorLine;

    @Lob
    @Column(name = "Status", nullable = false)
    private String status;

    @ColumnDefault("0")
    @Column(name = "ScoreEarned")
    private Float scoreEarned;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ExecutedAt")
    private Instant executedAt;

}