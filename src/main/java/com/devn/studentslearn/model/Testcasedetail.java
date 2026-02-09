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
@Table(name = "testcasedetails")
public class Testcasedetail {
    @Id
    @Column(name = "Id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "GroupId", nullable = false)
    private Testcasegroup group;

    @Column(name = "TestName", nullable = false)
    private String testName;

    @Lob
    @Column(name = "TestCode", nullable = false)
    private String testCode;

    @ColumnDefault("'pass'")
    @Lob
    @Column(name = "ExpectedResult")
    private String expectedResult;

    @ColumnDefault("1")
    @Column(name = "ScoreWeight", nullable = false)
    private Float scoreWeight;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CreatedAt")
    private Instant createdAt;

}