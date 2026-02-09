package com.devn.studentslearn.repository;

import com.devn.studentslearn.model.Codeexecutiondetail;

import com.devn.studentslearn.model.Codesubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeExecutionDetailRepository extends JpaRepository<Codeexecutiondetail, String> {
    List<Codeexecutiondetail> findBySubmission(Codesubmission submission);

}