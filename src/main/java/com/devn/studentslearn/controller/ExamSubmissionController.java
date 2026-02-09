package com.devn.studentslearn.controller;

import com.devn.studentslearn.dto.request.SubmitExamRequestDTO;
import com.devn.studentslearn.dto.reponse.ExamGradingResultResponseDTO; // ← SỬA CHỖ NÀY
import com.devn.studentslearn.model.Examresult;
import com.devn.studentslearn.repository.ExamresultRepository;
import com.devn.studentslearn.service.implement.IExamSubmissionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exams")
public class ExamSubmissionController {

    private final IExamSubmissionService examSubmissionService;
    private final ExamresultRepository examresultRepository;

    public ExamSubmissionController(
            IExamSubmissionService examSubmissionService,
            ExamresultRepository examresultRepository
    ) {
        this.examSubmissionService = examSubmissionService;
        this.examresultRepository = examresultRepository;
    }

    @PostMapping("/submit")
    public ResponseEntity<ExamGradingResultResponseDTO> submitExam(
            @Valid @RequestBody SubmitExamRequestDTO request
    ) {
        String resultId = examSubmissionService.submitExam(request);

        Examresult result = examresultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Exam result not found"));

        ExamGradingResultResponseDTO dto = new ExamGradingResultResponseDTO(
                result.getId(),
                result.getTotalQuestions(),
                result.getCorrectAnswers(),
                result.getTotalScore(),
                result.getBloomScore(),
                result.getExamDate()
        );

        return ResponseEntity.ok(dto);
    }
}