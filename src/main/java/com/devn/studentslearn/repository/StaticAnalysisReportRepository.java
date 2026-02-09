package com.devn.studentslearn.repository;

import com.devn.studentslearn.model.Staticanalysisreport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaticAnalysisReportRepository extends JpaRepository<Staticanalysisreport, String> {
    List<Staticanalysisreport> findBySubmissionId(String submissionId);
}