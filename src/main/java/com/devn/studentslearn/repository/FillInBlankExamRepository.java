package com.devn.studentslearn.repository;

import com.devn.studentslearn.model.Fillinblankexam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FillInBlankExamRepository extends JpaRepository<Fillinblankexam,String> {
    Fillinblankexam findByQuestionId(String questionId);
}
