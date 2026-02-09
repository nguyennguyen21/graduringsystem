package com.devn.studentslearn.grading;

import com.devn.studentslearn.grading.implement.IMultipleChoiceGrading;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MultipleChoiceGrading implements IMultipleChoiceGrading {
    @Override
    public boolean isAnswerCorrect(String userAnswer, Set<String> correctOptionLabels) {
        if (userAnswer == null || correctOptionLabels == null || correctOptionLabels.isEmpty()) {
            return false;
        }

        // Làm sạch input: hỗ trợ "A", "A,B", "[A, B]", "A, B, C"
        String clean = userAnswer.replaceAll("[\\[\\]\"{} ]", "").toUpperCase();
        String[] submitted = clean.split(",");
        Set<String> submittedSet = Stream.of(submitted)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());


        return submittedSet.equals(correctOptionLabels); 

    }
}
