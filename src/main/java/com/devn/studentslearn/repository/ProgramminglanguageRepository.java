package com.devn.studentslearn.repository;

import com.devn.studentslearn.model.Programminglanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramminglanguageRepository extends JpaRepository<Programminglanguage, Integer> {
}
