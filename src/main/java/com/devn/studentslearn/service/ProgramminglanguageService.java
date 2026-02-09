package com.devn.studentslearn.service;

import com.devn.studentslearn.model.Programminglanguage;
import com.devn.studentslearn.repository.ProgramminglanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramminglanguageService {

    @Autowired
    private ProgramminglanguageRepository repository;

    // Lấy tất cả ngôn ngữ (Sắp xếp theo tên để hiển thị dropdown đẹp hơn)
    public List<Programminglanguage> getAllLanguages() {
        return repository.findAll();
    }

    // Lấy chi tiết một ngôn ngữ theo ID
    public Optional<Programminglanguage> getLanguageById(Integer id) {
        return repository.findById(id);
    }

    // Thêm hoặc cập nhật ngôn ngữ mới
    public Programminglanguage saveLanguage(Programminglanguage language) {
        return repository.save(language);
    }

    // Xóa ngôn ngữ
    public void deleteLanguage(Integer id) {
        repository.deleteById(id);
    }
}