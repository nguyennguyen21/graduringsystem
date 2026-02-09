package com.devn.studentslearn.repository;

import com.devn.studentslearn.model.Multiplechoiceexam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MultipleChoiceExamRepository extends JpaRepository<Multiplechoiceexam, String> {
    Optional<Multiplechoiceexam> findByQuestionId(String questionId);
}