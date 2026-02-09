package com.devn.studentslearn.controller;

import com.devn.studentslearn.dto.request.CodeSubmissionRequest;
import com.devn.studentslearn.model.*;
import com.devn.studentslearn.service.CodeSubmissionService;
import com.devn.studentslearn.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/submit")
public class CodeSubmissionController {

    private final CodeSubmissionService codeSubmissionService;
    private final CodeSubmissionRepository codeSubmissionRepository;
    private final CodeExecutionDetailRepository codeExecutionDetailRepository;
    private final StaticAnalysisReportRepository staticAnalysisReportRepository;

    public CodeSubmissionController(
            CodeSubmissionService codeSubmissionService,
            CodeSubmissionRepository codeSubmissionRepository,
            CodeExecutionDetailRepository codeExecutionDetailRepository,
            StaticAnalysisReportRepository staticAnalysisReportRepository
    ) {
        this.codeSubmissionService = codeSubmissionService;
        this.codeSubmissionRepository = codeSubmissionRepository;
        this.codeExecutionDetailRepository = codeExecutionDetailRepository;
        this.staticAnalysisReportRepository = staticAnalysisReportRepository;
    }

    @PostMapping("/{questionId}")
    public ResponseEntity<Codesubmission> submitCode(
            @PathVariable String questionId,
            @Valid @RequestBody CodeSubmissionRequest request) {
        Codesubmission submission = codeSubmissionService.submitCode(questionId, request);
        return ResponseEntity.ok(submission);

    }

    @GetMapping("/{submissionId}/full-report")
    public ResponseEntity<Map<String, Object>> getFullReport(@PathVariable String submissionId) {
        Codesubmission submission = codeSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        List<Codeexecutiondetail> details = codeExecutionDetailRepository.findBySubmission(submission);
        List<Staticanalysisreport> reports = staticAnalysisReportRepository.findBySubmissionId(submissionId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", submission.getId());
        response.put("status", submission.getStatus() != null ? submission.getStatus() : "unknown");
        response.put("totalScore", submission.getTotalScore());

        // ✅ executionDetails — xử lý null an toàn
        response.put("executionDetails", details.stream().map(d -> {
            Map<String, Object> detail = new HashMap<>();
            detail.put("isCorrect", d.getIsCorrect() != null ? d.getIsCorrect() : false);
            detail.put("executionTimeMs", d.getExecutionTimeMs() != null ? d.getExecutionTimeMs() : 0);
            detail.put("memoryUsedKb", d.getMemoryUsedKb() != null ? d.getMemoryUsedKb() : 0);
            detail.put("actualOutput", d.getActualOutput() != null ? d.getActualOutput() : "");
            detail.put("status", d.getStatus() != null ? d.getStatus() : "unknown");
            return detail;
        }).toList());

        // ✅ analysis — xử lý null an toàn (đặc biệt lineNumber)
        response.put("analysis", reports.stream().map(r -> {
            Map<String, Object> issue = new HashMap<>();
            // lineNumber là Integer → có thể null
            issue.put("lineNumber", r.getLineNumber() != null ? r.getLineNumber() : -1);
            issue.put("message", r.getMessage() != null ? r.getMessage() : "");
            issue.put("issueSeverity", r.getIssueSeverity() != null ? r.getIssueSeverity() : "unknown");
            return issue;
        }).toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{submissionId}/debug")
    public ResponseEntity<?> debugReport(@PathVariable String submissionId) {
        Codesubmission submission = codeSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        List<Codeexecutiondetail> details = codeExecutionDetailRepository.findBySubmission(submission);

        return ResponseEntity.ok(Map.of(
                "submission", submission,
                "executionDetails", details
        ));
    }
}