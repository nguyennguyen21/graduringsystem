
package com.devn.studentslearn.strategy;

import com.devn.studentslearn.dto.request.BaseQuestionRequest;
import com.devn.studentslearn.dto.request.CreateFillInBlankQuestionRequestDTO;
import com.devn.studentslearn.model.Fillinblankexam;
import com.devn.studentslearn.model.Question;
import com.devn.studentslearn.repository.*;
import com.devn.studentslearn.strategy.implement.IQuestionStrategy;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Component
public class FillInBlankStrategy implements IQuestionStrategy {

    private final QuestionRepository questionRepository;
    private final FillInBlankExamRepository fillinblankexamRepository;
    private final ChapterRepository chapterRepository;
    private final UserRepository userRepository;

    public FillInBlankStrategy(
            QuestionRepository questionRepository,
            FillInBlankExamRepository fillinblankexamRepository,
            ChapterRepository chapterRepository,
            UserRepository userRepository
    ) {
        this.questionRepository = questionRepository;
        this.fillinblankexamRepository = fillinblankexamRepository;
        this.chapterRepository = chapterRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(String questionType) {
        return "fn".equals(questionType) || "fns".equals(questionType);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Question addQuestion(BaseQuestionRequest request) {
        if (!(request instanceof CreateFillInBlankQuestionRequestDTO dto)) {
            throw new IllegalArgumentException("Expected CreateFillInBlankQuestionRequestDTO");
        }

        Question question = new Question();
        question.setId(UUID.randomUUID().toString());
        question.setContent(dto.getContent());
        question.setBloomLevel(dto.getBloomLevel());
        question.setImg(dto.getImg());
        question.setQuestionType(dto.getQuestionType());

        var chapter = chapterRepository.findById(dto.getChapterId())
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found"));
        var user = userRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        question.setChapter(chapter);
        question.setCreatedBy(user);
        question.setCreatedAt(Instant.now());
        question.setUpdatedAt(Instant.now());

        // Lưu và gán lại biến để nhận các giá trị phát sinh từ DB (nếu có)
        Question savedQuestion = questionRepository.save(question);

        Fillinblankexam blank = new Fillinblankexam();
        blank.setId(UUID.randomUUID().toString());
        blank.setQuestion(savedQuestion);
        blank.setCorrectAnswer(dto.getCorrectAnswer()); 
        blank.setTolerance(dto.getTolerance());
        blank.setSynonyms(dto.getSynonyms());
        blank.setCreatedAt(Instant.now());

        fillinblankexamRepository.save(blank);

        // Trả về object question đã lưu thành công
        return savedQuestion;
    }

    @Override
    public void deleteQuestion(Question question) {
        questionRepository.delete(question);
    }

    @Override
    public void updateQuestion(Question question) {
        question.setUpdatedAt(Instant.now());
        questionRepository.save(question);
    }

    @Override
    public Optional<Question> getQuestionByQuestionId(String questionId) {
        return questionRepository.findById(questionId);
    }
}