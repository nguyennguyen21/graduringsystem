package com.devn.studentslearn.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CreateMultipleChoiceQuestionRequestDTO extends BaseQuestionRequest {

    @NotNull
    private String optionA;

    @NotNull
    private String optionB;

    @NotNull
    private String optionC;

    @NotNull
    private String optionD;

    @NotNull
    @Size(min = 1, max = 4, message = "Phải chọn ít nhất 1 và tối đa 4 đáp án đúng")
    private List<String> correctOptions;
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public List<String> getCorrectOptions() { return correctOptions; }
}