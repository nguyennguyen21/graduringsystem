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
@Table(name = "codetestcases")
public class Codetestcase {
    @Id
    @Column(name = "Id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "CodeContentId", nullable = false)
    private Codeeditorcontent codeContent;

    @Lob
    @Column(name = "Input")
    private String input;

    @Lob
    @Column(name = "ExpectedOutput", nullable = false)
    private String expectedOutput;

    @ColumnDefault("0")
    @Column(name = "IsSample")

    private Boolean isSample;

    @ColumnDefault("1")
    @Column(name = "ScoreWeight")
    private Float scoreWeight;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CreatedAt")
    private Instant createdAt;

}