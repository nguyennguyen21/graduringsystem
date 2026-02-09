package com.devn.studentslearn.repository;

import com.devn.studentslearn.model.Chapter;
import com.devn.studentslearn.model.Codeeditorcontent;
import com.devn.studentslearn.model.Programminglanguage;
import com.devn.studentslearn.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeEditorContentRepository extends JpaRepository<Codeeditorcontent,String> {
    List<Codeeditorcontent> findByQuestion(Question question);
    Optional<Codeeditorcontent> findByQuestionAndLanguage(Question question, Programminglanguage language);
    Optional<Codeeditorcontent> findByQuestion_IdAndLanguage_Id(String questionId, Integer languageId);
    Optional<Object> findByQuestionIdAndLanguageId(String id, int i);
}

