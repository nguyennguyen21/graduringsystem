package com.devn.studentslearn.grading.implement;

import java.util.List;
import java.util.Map;

public interface IFillInBlankGrading {
    boolean isAnswerCorrect(
            String userAnswer,
            List<String> correctAnswer,
            Double tolerance,
            Map<String, List<String>> synonyms
    );


}
