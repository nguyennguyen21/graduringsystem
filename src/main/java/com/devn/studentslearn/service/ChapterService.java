package com.devn.studentslearn.service;

import com.devn.studentslearn.model.Chapter;
import com.devn.studentslearn.repository.ChapterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterService {

    @Autowired
    private ChapterRepository chapterRepository;

    public List<Chapter> getAllChapters() {
        return chapterRepository.findAll();
    }

    public Chapter getChapterById(Integer id) {
        return chapterRepository.findById(id).orElse(null);
    }
}