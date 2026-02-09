package com.devn.studentslearn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "bloomscoring")
public class Bloomscoring {
    @Id
    @Lob
    @Column(name = "BloomLevel", nullable = false)
    private String bloomLevel;

    @ColumnDefault("1")
    @Column(name = "ScoreMultiplier", nullable = false)
    private Float scoreMultiplier;

    @Column(name = "Description")
    private String description;

}