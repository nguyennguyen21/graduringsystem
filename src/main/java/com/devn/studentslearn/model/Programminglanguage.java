package com.devn.studentslearn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "programminglanguages")
public class Programminglanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Integer id;

    @Column(name = "Name", nullable = false, length = 50)
    private String name;

    @Column(name = "MonacoLanguageId", nullable = false, length = 50)
    private String monacoLanguageId;

    @Column(name = "FileExtension", nullable = false, length = 10)
    private String fileExtension;

    @Lob
    @Column(name = "TemplateCode")
    private String templateCode;

    @ColumnDefault("1")
    @Column(name = "IsActive")
    private Boolean isActive;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CreatedAt")
    private Instant createdAt;

}