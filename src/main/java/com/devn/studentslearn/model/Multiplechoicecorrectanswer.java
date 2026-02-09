package com.devn.studentslearn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "multiplechoicecorrectanswers")
public class Multiplechoicecorrectanswer {
    @Id
    @Column(name = "Id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ExamId", nullable = false)
    private Multiplechoiceexam exam;

    @Lob
    @Column(name = "CorrectOption", nullable = false)
    private String correctOption;

}