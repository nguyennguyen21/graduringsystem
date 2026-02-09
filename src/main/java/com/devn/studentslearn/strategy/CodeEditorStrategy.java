package com.devn.studentslearn.strategy;

import com.devn.studentslearn.dto.request.BaseQuestionRequest;
import com.devn.studentslearn.dto.request.CreateCodeQuestionRequestDTO;
import com.devn.studentslearn.model.*;
import com.devn.studentslearn.repository.*;
import com.devn.studentslearn.strategy.implement.IQuestionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CodeEditorStrategy implements IQuestionStrategy {

    private final QuestionRepository questionRepository;
    private final CodeEditorContentRepository codeContentRepository;
    private final ChapterRepository chapterRepository;
    private final UserRepository userRepository;
    private final ProgramminglanguageRepository programmingLanguageRepository;
    private final CodeTestCaseRepository codetestcaseRepository;

    @Override
    public boolean supports(String questionType) {
        return "ce".equals(questionType) || "ces".equals(questionType);
    }

    @Override
    @Transactional // Rất quan trọng để đảm bảo tính toàn vẹn dữ liệu
    public Question addQuestion(BaseQuestionRequest request) {
        if (!(request instanceof CreateCodeQuestionRequestDTO dto)) {
            throw new IllegalArgumentException("Expected CreateCodeQuestionRequestDTO");
        }


        Question question = new Question();
        question.setId(UUID.randomUUID().toString());
        question.setContent(dto.getContent());
        question.setBloomLevel(dto.getBloomLevel());
        question.setQuestionType(dto.getQuestionType());
        question.setCreatedAt(Instant.now());


        question.setChapter(chapterRepository.getReferenceById(dto.getChapterId()));
        question.setCreatedBy(userRepository.getReferenceById(dto.getCreatedById()));

        Question savedQuestion = questionRepository.save(question);

        // 2. Khởi tạo và lưu Code Editor Content
        Codeeditorcontent codeContent = new Codeeditorcontent();
        codeContent.setId(UUID.randomUUID().toString());
        codeContent.setQuestion(savedQuestion);
        codeContent.setLanguage(programmingLanguageRepository.getReferenceById(dto.getLanguageId()));
        codeContent.setAppendCode(dto.getAppendCode());
        codeContent.setStarterCode(dto.getStarterCode());
        codeContent.setPrependCode(dto.getPrependCode());
        codeContent.setFunctionSignature(dto.getFunctionSignature());
        codeContent.setTimeLimitMs(dto.getTimeLimitMs());
        codeContent.setMemoryLimitMb(dto.getMemoryLimitMb());
        codeContent.setTestType(dto.getTestType());
        codeContent.setHeaderFiles(dto.getHeaderFiles());
        codeContent.setCreatedAt(Instant.now());

        Codeeditorcontent savedContent = codeContentRepository.save(codeContent);


        if (dto.getTestCases() != null && !dto.getTestCases().isEmpty()) {
            List<Codetestcase> testCaseList = new ArrayList<>();

            for (var tcDto : dto.getTestCases()) {
                Codetestcase tc = new Codetestcase();
                tc.setId(UUID.randomUUID().toString());
                tc.setCodeContent(savedContent);
                tc.setInput(tcDto.getInput());
                tc.setExpectedOutput(tcDto.getExpectedOutput());
                tc.setIsSample(tcDto.getIsSample() != null && tcDto.getIsSample());
                tc.setScoreWeight(tcDto.getScoreWeight() != null ? tcDto.getScoreWeight() : 1.0f);
                tc.setCreatedAt(Instant.now());

                testCaseList.add(tc);
            }

            codetestcaseRepository.saveAll(testCaseList);
        }

        return savedQuestion;
    }

    @Override
    @Transactional
    public void deleteQuestion(Question question) {
        questionRepository.delete(question);
    }

    @Override
    @Transactional
    public void updateQuestion(Question question) {
        question.setUpdatedAt(Instant.now());
        questionRepository.save(question);
    }

    @Override
    public Optional<Question> getQuestionByQuestionId(String questionId) {
        return questionRepository.findById(questionId);
    }
}