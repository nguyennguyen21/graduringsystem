package com.devn.studentslearn.dto.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "questionType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreateFillInBlankQuestionRequestDTO.class, name = "fn"),
        @JsonSubTypes.Type(value = CreateFillInBlankQuestionRequestDTO.class, name = "fns"),
        @JsonSubTypes.Type(value = CreateCodeQuestionRequestDTO.class, name = "ce"),
        @JsonSubTypes.Type(value = CreateCodeQuestionRequestDTO.class, name = "ces"),
        @JsonSubTypes.Type(value = CreateMultipleChoiceQuestionRequestDTO.class, name = "mcq"),
        @JsonSubTypes.Type(value = CreateMultipleChoiceQuestionRequestDTO.class, name = "mcqs"),
})
public abstract class BaseQuestionRequest {
    private String questionType;
    private String content;
    private String bloomLevel;
    private Integer chapterId;
    private String createdById;
    private String img;
}