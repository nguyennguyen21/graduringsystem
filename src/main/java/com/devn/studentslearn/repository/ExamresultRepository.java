package com.devn.studentslearn.repository;

import com.devn.studentslearn.model.Examresult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamresultRepository extends JpaRepository<Examresult, String> {
    List<Examresult> findByUserId(String userId);
}