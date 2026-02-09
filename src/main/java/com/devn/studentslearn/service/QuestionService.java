package com.devn.studentslearn.service;

import com.devn.studentslearn.Factory.QuestionFactory;
import com.devn.studentslearn.dto.reponse.CreateCodeQuestionReponseDTO;
import com.devn.studentslearn.dto.reponse.MultipleChoiceQuestionResponseDTO;
import com.devn.studentslearn.dto.reponse.QuestionResponse;
import com.devn.studentslearn.dto.request.BaseQuestionRequest;
import com.devn.studentslearn.model.*;
import com.devn.studentslearn.repository.*;
import com.devn.studentslearn.strategy.implement.IQuestionStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionFactory questionFactory;
    private final QuestionRepository questionRepository;
    private final CodeEditorContentRepository editorRepo;
    private final CodeTestCaseRepository codeTestCaseRepository;

    // Thêm Repository cho trắc nghiệm
    private final MultipleChoiceExamRepository mcqExamRepo;
    private final MultipleChoiceCorrectAnswerRepository mcqAnswerRepo;

    @Transactional
    public Question createQuestion(BaseQuestionRequest request) {
        IQuestionStrategy strategy = questionFactory.getStrategy(request.getQuestionType());
        return strategy.addQuestion(request);
    }

    @Transactional
    public void deleteQuestion(String questionId) {
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isEmpty()) {
            throw new IllegalArgumentException("Question not found");
        }
        Question question = questionOpt.get();
        IQuestionStrategy strategy = questionFactory.getStrategy(question.getQuestionType());
        strategy.deleteQuestion(question);
    }

    @Transactional
    public void updateQuestion(Question updatedQuestion) {
        IQuestionStrategy strategy = questionFactory.getStrategy(updatedQuestion.getQuestionType());
        strategy.updateQuestion(updatedQuestion);
    }

    public Optional<Question> getQuestionById(String questionId) {
        return questionRepository.findById(questionId);
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // ✅ NÂNG CẤP: Giữ nguyên logic cũ, thêm xử lý MCQ và Fix lỗi Deserialize
    public Object getFullTaskDetails(String id) {
        Question qEntity = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy câu hỏi với ID: " + id));

        // ---------------------------------------------------------
        // NHÁNH 1: XỬ LÝ TRẮC NGHIỆM (MCQ)
        // ---------------------------------------------------------
        if ("mcq".equals(qEntity.getQuestionType())) {
            Multiplechoiceexam exam = mcqExamRepo.findByQuestionId(id).orElse(null);
            List<String> correctOptions = new ArrayList<>();
            if (exam != null) {
                // Fix lỗi incompatible types bằng cách ép kiểu rõ ràng
                correctOptions = mcqAnswerRepo.findByExamId(exam.getId())
                        .stream()
                        .map(ans -> ((Multiplechoicecorrectanswer) ans).getCorrectOption())
                        .collect(Collectors.toList());
            }
            return new MultipleChoiceQuestionResponseDTO(
                    qEntity.getId(),
                    qEntity.getContent(),
                    exam != null ? exam.getOptionA() : "",
                    exam != null ? exam.getOptionB() : "",
                    exam != null ? exam.getOptionC() : "",
                    exam != null ? exam.getOptionD() : "",
                    correctOptions,
                    qEntity.getCreatedAt()
            );
        }

        // ---------------------------------------------------------
        // NHÁNH 2: XỬ LÝ CODE EDITOR (GIỮ NGUYÊN LOGIC CỦA BẠN)
        // ---------------------------------------------------------
        CreateCodeQuestionReponseDTO res = new CreateCodeQuestionReponseDTO();
        res.setId(qEntity.getId());
        res.setContent(qEntity.getContent());
        res.setBloomLevel(qEntity.getBloomLevel());
        res.setChapterId(qEntity.getChapter() != null ? qEntity.getChapter().getId() : null);
        res.setCreatedById(qEntity.getCreatedBy() != null ? qEntity.getCreatedBy().getId() : null);
        res.setImg(qEntity.getImg());

        try {
            // Lỗi xảy ra ở đây do Jackson không parse được JSON trong DB
            Codeeditorcontent editorEntity = editorRepo.findByQuestion_IdAndLanguage_Id(id, 4)
                    .orElse(null);

            if (editorEntity != null) {
                res.setLanguageId(editorEntity.getLanguage() != null ? editorEntity.getLanguage().getId() : 4);
                res.setStarterCode(editorEntity.getStarterCode());
                res.setPrependCode(editorEntity.getPrependCode());
                res.setAppendCode(editorEntity.getAppendCode());
                res.setFunctionSignature(String.valueOf(editorEntity.getFunctionSignature()));
                res.setTestFramework(editorEntity.getTestFramework());
                res.setTimeLimitMs(editorEntity.getTimeLimitMs());
                res.setMemoryLimitMb(editorEntity.getMemoryLimitMb());
                res.setTestType(editorEntity.getTestType());
                res.setHeaderFiles(editorEntity.getHeaderFiles());

                List<Codetestcase> dbTestCases = codeTestCaseRepository.findByCodeContent(editorEntity);
                List<CreateCodeQuestionReponseDTO.TestCaseResponse> dtoTestCases = dbTestCases.stream()
                        .map(tc -> {
                            CreateCodeQuestionReponseDTO.TestCaseResponse dto = new CreateCodeQuestionReponseDTO.TestCaseResponse();
                            dto.setInput(tc.getInput());
                            dto.setExpectedOutput(tc.getExpectedOutput());
                            dto.setIsSample(tc.getIsSample());
                            dto.setScoreWeight(tc.getScoreWeight());
                            return dto;
                        })
                        .collect(Collectors.toList());
                res.setTestCases(dtoTestCases);
            } else {
                setDefaults(res);
            }
        } catch (Exception e) {
            // ✅ FIX LỖI: Nếu DB lưu sai định dạng JSON, trả về mặc định thay vì văng lỗi 500
            System.err.println("Lỗi dữ liệu JSON tại câu hỏi " + id + ": " + e.getMessage());
            setDefaults(res);
        }

        return res;
    }

    private void setDefaults(CreateCodeQuestionReponseDTO res) {
        res.setLanguageId(4);
        res.setStarterCode("// Bài tập chưa được cấu hình hoặc dữ liệu lỗi");
        res.setTestCases(new ArrayList<>());
        res.setTimeLimitMs(1000);
        res.setMemoryLimitMb(256);
        res.setTestType("unittest");
    }
}