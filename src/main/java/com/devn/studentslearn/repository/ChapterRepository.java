package com.devn.studentslearn.repository;

import com.devn.studentslearn.model.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

}
