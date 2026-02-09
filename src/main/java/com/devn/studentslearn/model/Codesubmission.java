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
@Table(name = "codesubmissions")
public class Codesubmission {
    @Id
    @Column(name = "Id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "QuestionId", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LanguageId", nullable = false)
    private Programminglanguage language;

    @Lob
    @Column(name = "SubmittedCode", nullable = false)
    private String submittedCode;

    @ColumnDefault("0")
    @Column(name = "TotalScore")
    private Float totalScore;

    @ColumnDefault("'pending'")
    @Lob
    @Column(name = "Status")
    private String status;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "SubmittedAt")
    private Instant submittedAt;

}