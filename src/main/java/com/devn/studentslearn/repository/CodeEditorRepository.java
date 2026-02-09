package com.devn.studentslearn.repository;

import com.devn.studentslearn.model.Codeeditorcontent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeEditorRepository extends JpaRepository<Codeeditorcontent,String> {
    @Override
    Optional<Codeeditorcontent> findById(String id);

    Optional<Codeeditorcontent> findCodeeditorcontentByQuestion(String id);
}
