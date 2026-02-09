package com.devn.studentslearn.dto.reponse;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CreateCodeQuestionReponseDTO {

    private String id;
    private String content;
    private String bloomLevel;
    private Integer chapterId;
    private String createdById;
    private String img;


    private Integer languageId;
    private String starterCode;
    private String prependCode;
    private String appendCode;
    private String functionSignature;
    private String testFramework;
    private Integer timeLimitMs;
    private Integer memoryLimitMb;
    private String testType;
    private Map<String, String> headerFiles;


    private List<TestCaseResponse> testCases;


    @Getter
    @Setter
    public static class TestCaseResponse {
        private String id;
        private String input;
        private String expectedOutput;
        private Boolean isSample;
        private Float scoreWeight;
    }

}