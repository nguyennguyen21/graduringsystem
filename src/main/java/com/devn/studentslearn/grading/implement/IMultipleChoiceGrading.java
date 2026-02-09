package com.devn.studentslearn.grading.implement;

import java.util.Set;

public interface IMultipleChoiceGrading {
    boolean isAnswerCorrect(String userAnswer, Set<String> correctOptionLabels);
}
