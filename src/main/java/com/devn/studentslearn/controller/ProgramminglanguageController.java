package com.devn.studentslearn.controller;

import com.devn.studentslearn.model.Programminglanguage;
import com.devn.studentslearn.service.ProgramminglanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/languages")
public class ProgramminglanguageController {

    @Autowired
    private ProgramminglanguageService service;

    // API: GET /api/languages
    @GetMapping
    public ResponseEntity<List<Programminglanguage>> getAll() {
        List<Programminglanguage> languages = service.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    // API: GET /api/languages/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Programminglanguage> getById(@PathVariable Integer id) {
        return service.getLanguageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // API: POST /api/languages (Dùng để thêm ngôn ngữ mới từ Admin)
    @PostMapping
    public ResponseEntity<Programminglanguage> create(@RequestBody Programminglanguage language) {
        return ResponseEntity.ok(service.saveLanguage(language));
    }

    // API: DELETE /api/languages/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteLanguage(id);
        return ResponseEntity.noContent().build();
    }
}