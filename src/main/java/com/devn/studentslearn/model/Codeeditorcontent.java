package com.devn.studentslearn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "codeeditorcontents")
public class Codeeditorcontent {
    @Id
    @Column(name = "Id", nullable = false, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "QuestionId", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "LanguageId", nullable = false)
    private Programminglanguage language;

    @Lob
    @Column(name = "StarterCode")
    private String starterCode;

    @ColumnDefault("2000")
    @Column(name = "TimeLimitMs")
    private Integer timeLimitMs;

    @ColumnDefault("256")
    @Column(name = "MemoryLimitMb")
    private Integer memoryLimitMb;

    @ColumnDefault("'io'")
    @Lob
    @Column(name = "TestType", nullable = false)
    private String testType;

    @Lob
    @Column(name = "PrependCode")
    private String prependCode;

    @Lob
    @Column(name = "AppendCode")
    private String appendCode;

    @Column(name = "HeaderFiles")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> headerFiles;

    @Column(name = "TestFramework", length = 50)
    private String testFramework;

    @Column(name = "FunctionSignature", length = 500)
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> functionSignature;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CreatedAt")
    private Instant createdAt;


}