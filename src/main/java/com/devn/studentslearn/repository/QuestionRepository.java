// package com.devn.studentslearn.repository;

package com.devn.studentslearn.repository;

import com.devn.studentslearn.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, String> {
    @Override
    List<Question> findAll();
}