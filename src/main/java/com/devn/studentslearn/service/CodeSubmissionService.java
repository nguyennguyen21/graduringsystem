// src/main/java/com/devn/studentslearn/service/CodeSubmissionService.java
package com.devn.studentslearn.service;

import com.devn.studentslearn.dto.request.CodeSubmissionRequest;
import com.devn.studentslearn.model.*;
import com.devn.studentslearn.repository.*;
import com.devn.studentslearn.sanbox.SandboxExecutor;
import com.devn.studentslearn.sanbox.SandboxResult;
import com.devn.studentslearn.sanbox.TestCaseResult;
import com.devn.studentslearn.sanbox.sanboxfactory.SandboxFactory;
import com.devn.studentslearn.sanbox.cpp.CppStaticAnalyzer;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class CodeSubmissionService {
    private static final Logger log = LoggerFactory.getLogger(CodeSubmissionService.class);

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final ProgramminglanguageRepository programmingLanguageRepository;
    private final CodeSubmissionRepository codeSubmissionRepository;
    private final CodeEditorContentRepository codeEditorContentRepository;
    private final CodeTestCaseRepository codeTestCaseRepository;
    private final CodeExecutionDetailRepository codeExecutionDetailRepository;
    private final SandboxFactory sandboxFactory;
    private final StaticAnalysisReportRepository staticAnalysisReportRepository;
    private final CppStaticAnalyzer cppStaticAnalyzer;

    public CodeSubmissionService(
            UserRepository userRepository,
            QuestionRepository questionRepository,
            ProgramminglanguageRepository programmingLanguageRepository,
            CodeSubmissionRepository codeSubmissionRepository,
            CodeEditorContentRepository codeEditorContentRepository,
            CodeTestCaseRepository codeTestCaseRepository,
            CodeExecutionDetailRepository codeExecutionDetailRepository,
            SandboxFactory sandboxFactory,
            StaticAnalysisReportRepository staticAnalysisReportRepository,
            CppStaticAnalyzer cppStaticAnalyzer
    ) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.programmingLanguageRepository = programmingLanguageRepository;
        this.codeSubmissionRepository = codeSubmissionRepository;
        this.codeEditorContentRepository = codeEditorContentRepository;
        this.codeTestCaseRepository = codeTestCaseRepository;
        this.codeExecutionDetailRepository = codeExecutionDetailRepository;
        this.sandboxFactory = sandboxFactory;
        this.staticAnalysisReportRepository = staticAnalysisReportRepository;
        this.cppStaticAnalyzer = cppStaticAnalyzer;
    }

    public Codesubmission submitCode(String questionId, CodeSubmissionRequest request) {
        String userId = request.getCreatedById();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found: " + questionId));
        Programminglanguage language = programmingLanguageRepository.findById(request.getLanguageId())
                .orElseThrow(() -> new IllegalArgumentException("Language not found: " + request.getLanguageId()));

        Codesubmission submission = createAndSavePendingSubmission(user, question, language, request);
        runCppCheckAnalysis(submission, request.getSubmittedCode());

        try {
            executeAndEvaluate(submission);
            submission.setStatus("completed");
        } catch (Exception e) {
            log.error("Execution failed for submission: {}", submission.getId(), e);
            markSubmissionAsError(submission);
        }

        return codeSubmissionRepository.save(submission);
    }

    @Transactional
    protected Codesubmission createAndSavePendingSubmission(User user, Question question, Programminglanguage language, CodeSubmissionRequest request) {
        Codesubmission submission = new Codesubmission();
        submission.setId(UUID.randomUUID().toString());
        submission.setUser(user);
        submission.setQuestion(question);
        submission.setLanguage(language);
        submission.setSubmittedCode(request.getSubmittedCode());
        submission.setTotalScore(0.0f);
        submission.setStatus("pending");
        submission.setSubmittedAt(Instant.now());
        return codeSubmissionRepository.save(submission);
    }

    private void markSubmissionAsError(Codesubmission submission) {
        submission.setStatus("error");
        submission.setTotalScore(0.0f);
    }

    // 🔥 ĐÃ SỬA: Không parse thủ công, truyền List<String> trực tiếp
    private void executeAndEvaluate(Codesubmission submission) {
        Codeeditorcontent codeContent = codeEditorContentRepository
                .findByQuestionAndLanguage(submission.getQuestion(), submission.getLanguage())
                .orElseThrow(() -> new IllegalStateException("Code content not found"));

        List<Codetestcase> testCases = codeTestCaseRepository.findByCodeContent(codeContent);
        if (testCases.isEmpty()) {
            throw new IllegalStateException("No test cases found for this code content");
        }

        // ✅ Lấy List<String> trực tiếp
        List<String> functionSignatures = codeContent.getFunctionSignature();
        if (functionSignatures == null || functionSignatures.isEmpty()) {
            throw new IllegalStateException("Function signature is missing for code content: " + codeContent.getId());

        }


        // ✅ Truyền đúng key "functionSignatures"
        Map<String, Object> config = Map.of("functionSignatures", functionSignatures);

        SandboxExecutor executor = sandboxFactory.getExecutor(submission.getLanguage().getId().toString());
        SandboxResult result = executor.execute(submission.getSubmittedCode(), testCases, config);

        if (result.getTestCaseResults().size() != testCases.size()) {
            throw new IllegalStateException("Sandbox returned invalid number of test results");
        }

        float totalScore = 0.0f;
        for (int i = 0; i < testCases.size(); i++) {
            Codetestcase testCase = testCases.get(i);
            TestCaseResult tr = result.getTestCaseResults().get(i);

            Codeexecutiondetail detail = new Codeexecutiondetail();
            detail.setId(UUID.randomUUID().toString());
            detail.setSubmission(submission);
            detail.setTestCase(testCase);
            detail.setActualOutput(tr.getActualOutput());
            detail.setIsCorrect(tr.isCorrect());
            detail.setExecutionTimeMs(tr.getExecutionTimeMs());
            detail.setMemoryUsedKb(tr.getMemoryUsedKb());
            detail.setErrorMessage(tr.getErrorMessage());
            detail.setStatus(tr.isCorrect() ? "passed" : "failed");
            detail.setScoreEarned(tr.isCorrect() ? testCase.getScoreWeight() : 0.0f);
            detail.setExecutedAt(Instant.now());

            codeExecutionDetailRepository.save(detail);
            if (tr.isCorrect()) {
                totalScore += testCase.getScoreWeight();
            }
        }

        submission.setTotalScore(totalScore);
    }



    private void runCppCheckAnalysis(Codesubmission submission, String userCode) {
        if (!"cpp".equals(submission.getLanguage().getMonacoLanguageId())) {
            return;
        }

        try {
            String tempDir = System.getProperty("java.io.tmpdir");
            String sourcePath = tempDir + "/sub_" + submission.getId() + ".cpp";
            java.nio.file.Path sourceFile = java.nio.file.Paths.get(sourcePath);
            java.nio.file.Files.write(sourceFile, userCode.getBytes());

            var result = cppStaticAnalyzer.analyze(sourcePath);

            for (var issue : result.getIssues()) {
                var report = new Staticanalysisreport();
                report.setId(java.util.UUID.randomUUID().toString());
                report.setSubmission(submission);
                report.setIssueSeverity(issue.getSeverity().toLowerCase());
                try {
                    report.setLineNumber(Integer.parseInt(issue.getLineNumber()));
                } catch (NumberFormatException ignored) {
                }
                report.setMessage(issue.getMessage());
                staticAnalysisReportRepository.save(report);
            }

            java.nio.file.Files.deleteIfExists(sourceFile);
        } catch (Exception e) {
            log.warn("Cppcheck failed for {}: {}", submission.getId(), e.getMessage());
        }
    }
}