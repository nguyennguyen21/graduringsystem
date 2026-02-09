// File: src/main/java/com/devn/studentslearn/sanbox/cpp/GoogleTestParser.java

package com.devn.studentslearn.sanbox.cpp;

import com.devn.studentslearn.sanbox.TestCaseResult;
import com.devn.studentslearn.sanbox.cpp.model.GTestResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GoogleTestParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Pattern ACTUAL_PATTERN = Pattern.compile("Actual: ([\\s\\S]*?)(?=\\n|$)");


    public static List<TestCaseResult> parse(Path jsonFile, int numTestCases) throws IOException {
        GTestResult result = objectMapper.readValue(jsonFile.toFile(), GTestResult.class);
        List<TestCaseResult> parsedResults = new ArrayList<>();
        List<GTestResult.TestCase> gtestCases = new ArrayList<>();

        // Safely extract test cases from: testsuites[0].testsuite
        if (result.getTestsuites() != null && !result.getTestsuites().isEmpty()) {
            var suite = result.getTestsuites().get(0);
            if (suite != null && suite.getTestsuite() != null) {
                gtestCases = suite.getTestsuite();
            }
        }

        // Map each expected test case to a result
        for (int i = 0; i < numTestCases; i++) {
            if (i < gtestCases.size()) {
                var tc = gtestCases.get(i);
                boolean passed = "COMPLETED".equals(tc.getResult());
                String actual = "";
                String errorMsg = "";

                // Extract error details if test failed
                if (!passed && tc.getFailures() != null && !tc.getFailures().isEmpty()) {
                    String failureMsg = tc.getFailures().get(0).getFailure();
                    errorMsg = failureMsg != null ? failureMsg : "Unknown failure";
                    actual = extractValue(errorMsg, ACTUAL_PATTERN);
                }

                // Execution time and memory will be filled later by sandbox executor
                parsedResults.add(new TestCaseResult(passed, actual, errorMsg, 0, 0));
            } else {
                // JSON contains fewer test results than expected
                parsedResults.add(new TestCaseResult(false, "", "Test not executed", 0, 0));
            }
        }

        return parsedResults;
    }


    private static String extractValue(String text, Pattern pattern) {
        if (text == null) {
            return "";
        }
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }
}