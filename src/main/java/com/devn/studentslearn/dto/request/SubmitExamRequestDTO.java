// dto/request/SubmitExamRequestDTO.java
package com.devn.studentslearn.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class SubmitExamRequestDTO {

    @NotBlank
    private String userId;

    @Valid
    @NotNull
    private List<AnswerItem> answers;

    @Getter
    @Setter
    public static class AnswerItem {
        @NotBlank
        private String questionId;

        private String userAnswer;
    }
}