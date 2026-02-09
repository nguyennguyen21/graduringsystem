package com.devn.studentslearn.repository;

import com.devn.studentslearn.model.Multiplechoicecorrectanswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface MultipleChoiceCorrectAnswerRepository extends JpaRepository<Multiplechoicecorrectanswer, String> {
    void deleteByExamId(String examId);
    @Query("SELECT a FROM Multiplechoicecorrectanswer a WHERE a.exam.question.id = :questionId")
    List<Multiplechoicecorrectanswer> findByQuestionId(String questionId);

    Collection<Object> findByExamId(String id);
}