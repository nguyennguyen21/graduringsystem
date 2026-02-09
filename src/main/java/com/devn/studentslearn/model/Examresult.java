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
@Table(name = "examresults")
public class Examresult {
    @Id
    @Column(name = "Id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @Column(name = "TotalScore", nullable = false)
    private Float totalScore;

    @Column(name = "TotalQuestions", nullable = false)
    private Integer totalQuestions;

    @Column(name = "CorrectAnswers", nullable = false)
    private Integer correctAnswers;

    @ColumnDefault("0")
    @Column(name = "BloomScore")
    private Float bloomScore;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "ExamDate")
    private Instant examDate;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CreatedAt")
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;

    }

}