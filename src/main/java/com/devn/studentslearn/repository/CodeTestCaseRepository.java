package com.devn.studentslearn.repository;


import com.devn.studentslearn.model.Codeeditorcontent;
import com.devn.studentslearn.model.Codetestcase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeTestCaseRepository extends JpaRepository<Codetestcase, String> {
    List<Codetestcase> findByCodeContent(Codeeditorcontent codeContent);



}