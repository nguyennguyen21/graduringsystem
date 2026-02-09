package com.devn.studentslearn.grading;

import com.devn.studentslearn.grading.implement.IFillInBlankGrading;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public class FillInBlankGrading implements IFillInBlankGrading {

    private static final MathContext MATH_CONTEXT = new MathContext(15, RoundingMode.HALF_UP);


    @Override
    public boolean isAnswerCorrect(
            String userAnswer,
            List<String> correctAnswers,
            Double tolerance,
            Map<String, List<String>> synonyms
    ) {
        if (userAnswer == null || correctAnswers == null || correctAnswers.isEmpty()) {
            return false;
        }

        String ua = userAnswer.trim();
        if (ua.isEmpty()) {
            return false;
        }


        BigDecimal allowedTol = BigDecimal.ZERO;
        if (tolerance != null && tolerance >= 0) {
            allowedTol = new BigDecimal(tolerance.toString(), MATH_CONTEXT);
        }

        for (String ca : correctAnswers) {
            if (ca == null) continue;

            String correctAns = ca.trim();
            if (correctAns.isEmpty()) continue;

            try {
                BigDecimal userNum = new BigDecimal(ua, MATH_CONTEXT);
                BigDecimal correctNum = new BigDecimal(correctAns, MATH_CONTEXT);
                BigDecimal diff = userNum.subtract(correctNum).abs();

                if (diff.compareTo(allowedTol) <= 0) {
                    return true;
                }
            } catch (NumberFormatException ignored) {

            }


            if (ua.equalsIgnoreCase(correctAns)) {
                return true;
            }


            if (synonyms != null) {
                List<String> synonymList = synonyms.get(correctAns);
                if (synonymList != null) {
                    for (String syn : synonymList) {
                        if (syn != null && ua.equalsIgnoreCase(syn.trim())) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }





}