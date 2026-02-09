package com.devn.studentslearn.service;

import com.devn.studentslearn.dto.request.SubmitExamRequestDTO;
import com.devn.studentslearn.grading.FillInBlankGrading;
import com.devn.studentslearn.grading.MultipleChoiceGrading;
import com.devn.studentslearn.model.*;
import com.devn.studentslearn.model.Multiplechoicecorrectanswer;
import com.devn.studentslearn.repository.*;
import com.devn.studentslearn.repository.MultipleChoiceCorrectAnswerRepository;
import com.devn.studentslearn.service.implement.IExamSubmissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExamSubmissionService implements IExamSubmissionService {

    private final ExamresultRepository examresultRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final FillInBlankExamRepository fillInBlankExamRepository;
    private final MultipleChoiceCorrectAnswerRepository multipleChoiceAnswerRepository;
    private final FillInBlankGrading fillInBlankGradingService;
    private final MultipleChoiceGrading multipleChoiceGradingService;

    public ExamSubmissionService(
            ExamresultRepository examresultRepository,
            UserRepository userRepository,
            QuestionRepository questionRepository,
            FillInBlankExamRepository fillInBlankExamRepository,
            MultipleChoiceCorrectAnswerRepository multipleChoiceAnswerRepository,
            FillInBlankGrading fillInBlankGradingService,
            MultipleChoiceGrading multipleChoiceGradingService
    ) {
        this.examresultRepository = examresultRepository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.fillInBlankExamRepository = fillInBlankExamRepository;
        this.multipleChoiceAnswerRepository = multipleChoiceAnswerRepository;
        this.fillInBlankGradingService = fillInBlankGradingService;
        this.multipleChoiceGradingService = multipleChoiceGradingService;
    }

    @Override
    public String submitExam(SubmitExamRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        int totalQuestions = request.getAnswers().size();
        int correctAnswers = 0;
        float bloomScore = 0.0f; // ← THÊM: tích lũy BloomScore

        for (var answerItem : request.getAnswers()) {
            String questionId = answerItem.getQuestionId();
            String userAnswer = answerItem.getUserAnswer();

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));

            boolean isCorrect;

            switch (question.getQuestionType()) {
                case "fn", "fns" -> {
                    Fillinblankexam blank = fillInBlankExamRepository.findByQuestionId(questionId);
                    if (blank == null) {
                        throw new RuntimeException("Fill-in-blank data not found for question: " + questionId);
                    }
                    isCorrect = fillInBlankGradingService.isAnswerCorrect(
                            userAnswer,
                            blank.getCorrectAnswer(),
                            blank.getTolerance(),
                            blank.getSynonyms()
                    );
                }
                case "mc" -> {
                    List<Multiplechoicecorrectanswer> correctAnswersList =
                            multipleChoiceAnswerRepository.findByQuestionId(questionId);

                    Set<String> correctLabels = correctAnswersList.stream()
                            .map(Multiplechoicecorrectanswer::getCorrectOption)
                            .map(opt -> opt != null ? opt.toUpperCase() : "")
                            .filter(opt -> !opt.isEmpty())
                            .collect(Collectors.toSet());

                    isCorrect = multipleChoiceGradingService.isAnswerCorrect(userAnswer, correctLabels);
                }
                default -> isCorrect = false;
            }

            if (isCorrect) {
                correctAnswers++;
                // ← THÊM: cộng BloomScore theo cấp độ
                bloomScore += getBloomMultiplier(question.getBloomLevel());
            }
        }

        Examresult result = new Examresult();
        result.setId(UUID.randomUUID().toString());
        result.setUser(user);
        result.setTotalQuestions(totalQuestions);
        result.setCorrectAnswers(correctAnswers);
        result.setTotalScore((float) correctAnswers); // 1 điểm / câu đúng
        result.setBloomScore(bloomScore); // ← ĐÃ CẬP NHẬT
        result.setExamDate(Instant.now());
        result.setCreatedAt(Instant.now());

        examresultRepository.save(result);
        return result.getId();
    }


    private float getBloomMultiplier(String bloomLevel) {
        if (bloomLevel == null) return 1.0f;
        return switch (bloomLevel) {
            case "r" -> 1.0f;
            case "u" -> 1.2f;
            case "ap" -> 1.5f;
            case "an" -> 2.0f;
            default -> 1.0f;
        };
    }
}