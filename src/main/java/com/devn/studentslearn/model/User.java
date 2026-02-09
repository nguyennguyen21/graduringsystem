package com.devn.studentslearn.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "Id", nullable = false, length = 36)
    private String id;

    @Column(name = "Username", nullable = false, length = 36)
    private String username;

    @Column(name = "Fullname", nullable = false)
    private String fullname;

    @Column(name = "PasswordHash", nullable = false)
    private String passwordHash;

    @ColumnDefault("'student'")
    @Lob
    @Column(name = "Role")
    private String role = "student";

    @ColumnDefault("0")
    @Column(name = "Exp")
    private Integer exp;

    @ManyToOne(fetch = FetchType.LAZY)
    @ColumnDefault("1")
    @JoinColumn(name = "BadgeId")
    private Badge badge;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "CreatedAt")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "UpdatedAt")
    private Instant updatedAt;


    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }


    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }


}