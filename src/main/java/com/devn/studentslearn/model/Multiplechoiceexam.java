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
@Table(name = "multiplechoiceexams")
public class Multiplechoiceexam {
    @Id
    @Column(name = "Id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "QuestionId", nullable = false)
    private Question question;

    @Lob
    @Column(name = "OptionA", nullable = false)
    private String optionA;

    @Lob
    @Column(name = "OptionB", nullable = false)
    private String optionB;

    @Lob
    @Column(name = "OptionC", nullable = false)
    private String optionC;

    @Lob
    @Column(name = "OptionD", nullable = false)
    private String optionD;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CreatedAt")
    private Instant createdAt;

}