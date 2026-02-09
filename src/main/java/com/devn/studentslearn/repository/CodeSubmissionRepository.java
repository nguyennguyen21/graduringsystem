package com.devn.studentslearn.repository;

import com.devn.studentslearn.model.Codesubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeSubmissionRepository extends JpaRepository<Codesubmission, String> {
    List<Codesubmission> findByUserIdAndQuestionId(String userId, String questionId);
}