package com.devn.studentslearn.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CreateCodeQuestionRequestDTO extends BaseQuestionRequest {

    @NotNull
    private Integer languageId;
    private String starterCode;
    private String prependCode;
    private String appendCode;
    private List<String> functionSignature; // ✅ Jackson tự map từ JSON array
    private String testFramework;

    @Min(100)
    private Integer timeLimitMs = 2000;

    @Min(16)
    private Integer memoryLimitMb = 256;

    @NotBlank
    private String testType = "unittest";

    private Map<String, String> headerFiles;

    @Valid
    private List<TestCase> testCases;

    @Getter
    @Setter
    public static class TestCase {
        private String input;
        @NotBlank
        private String expectedOutput;
        private Boolean isSample = false;
        private Float scoreWeight = 1.0f;
    }

    // ❌ KHÔNG CẦN VIẾT THỦ CÔNG CÁC GETTER/SETTER NỮA — ĐÃ CÓ LOMBOK
}