package com.devn.studentslearn.strategy;

import com.devn.studentslearn.dto.request.BaseQuestionRequest;
import com.devn.studentslearn.dto.request.CreateMultipleChoiceQuestionRequestDTO;
import com.devn.studentslearn.model.*;
import com.devn.studentslearn.repository.*;
import com.devn.studentslearn.strategy.implement.IQuestionStrategy;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MultipleChoiceStrategy implements IQuestionStrategy {

    private final QuestionRepository questionRepository;
    private final ChapterRepository chapterRepository;
    private final UserRepository userRepository;
    private final MultipleChoiceExamRepository multipleChoiceExamRepository;
    private final MultipleChoiceCorrectAnswerRepository multipleChoiceCorrectAnswerRepository;

    public MultipleChoiceStrategy(
            QuestionRepository questionRepository,
            ChapterRepository chapterRepository,
            UserRepository userRepository,
            MultipleChoiceExamRepository multipleChoiceExamRepository,
            MultipleChoiceCorrectAnswerRepository multipleChoiceCorrectAnswerRepository) {
        this.questionRepository = questionRepository;
        this.chapterRepository = chapterRepository;
        this.userRepository = userRepository;
        this.multipleChoiceExamRepository = multipleChoiceExamRepository;
        this.multipleChoiceCorrectAnswerRepository = multipleChoiceCorrectAnswerRepository;
    }

    @Override
    public boolean supports(String questionType) {
        return "mcq".equals(questionType) || "mcqs".equals(questionType);
    }

    @Override
    @Transactional
    public Question addQuestion(BaseQuestionRequest request) {
        if (!(request instanceof CreateMultipleChoiceQuestionRequestDTO dto)) {
            throw new IllegalArgumentException("Invalid request type for MCQ strategy");
        }

        // Validate correct options early
        validateAndNormalizeCorrectOptions(dto.getCorrectOptions());

        var chapter = chapterRepository.findById(dto.getChapterId())
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found: " + dto.getChapterId()));
        var creator = userRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + dto.getCreatedById()));

        Question question = new Question();
        question.setId(UUID.randomUUID().toString());
        question.setContent(dto.getContent());
        question.setChapter(chapter);
        question.setCreatedBy(creator);
        question.setBloomLevel(dto.getBloomLevel());
        question.setQuestionType("mcq");
        question.setCreatedAt(Instant.now());
        question = questionRepository.save(question);

        // Create MultipleChoiceExam
        Multiplechoiceexam exam = new Multiplechoiceexam();
        exam.setId(UUID.randomUUID().toString());
        exam.setQuestion(question);
        exam.setOptionA(dto.getOptionA());
        exam.setOptionB(dto.getOptionB());
        exam.setOptionC(dto.getOptionC());
        exam.setOptionD(dto.getOptionD());
        exam.setCreatedAt(Instant.now());
        multipleChoiceExamRepository.save(exam);

        // Save correct answers (already validated)
        List<Multiplechoicecorrectanswer> correctAnswers = dto.getCorrectOptions().stream()
                .map(opt -> {
                    String normalized = opt.strip().toUpperCase();
                    Multiplechoicecorrectanswer ans = new Multiplechoicecorrectanswer();
                    ans.setId(UUID.randomUUID().toString());
                    ans.setExam(exam);
                    ans.setCorrectOption(normalized); // safe: only A/B/C/D
                    return ans;
                })
                .collect(Collectors.toList());

        multipleChoiceCorrectAnswerRepository.saveAll(correctAnswers);

        return question;
    }

    private void validateAndNormalizeCorrectOptions(List<String> options) {
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("At least one correct option must be provided");
        }

        Set<String> validOptions = Set.of("A", "B", "C", "D");
        for (String opt : options) {
            if (opt == null) {
                throw new IllegalArgumentException("Correct option cannot be null");
            }
            String clean = opt.strip().toUpperCase();
            if (clean.length() != 1 || !validOptions.contains(clean)) {
                throw new IllegalArgumentException(
                        "Invalid correct option: '" + opt + "'. Must be one of: A, B, C, D.");
            }
        }
    }

    @Override
    @Transactional
    public void deleteQuestion(Question question) {
        var examOpt = multipleChoiceExamRepository.findByQuestionId(question.getId());
        if (examOpt.isPresent()) {
            Multiplechoiceexam exam = examOpt.get();
            multipleChoiceCorrectAnswerRepository.deleteByExamId(exam.getId());
            multipleChoiceExamRepository.delete(exam);
        }
        questionRepository.delete(question);
    }

    @Override
    @Transactional
    public void updateQuestion(Question question) {
        questionRepository.save(question);
    }

    @Override
    public Optional<Question> getQuestionByQuestionId(String questionId) {
        return questionRepository.findById(questionId);
    }
}