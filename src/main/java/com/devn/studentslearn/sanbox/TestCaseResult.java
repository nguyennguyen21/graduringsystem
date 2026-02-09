// File: src/main/java/com/devn/studentslearn/sanbox/TestCaseResult.java

package com.devn.studentslearn.sanbox;

public class TestCaseResult {
    private final boolean passed;
    private final String actualOutput;
    private final String errorMessage;
    private final int executionTimeMs;
    private final int memoryUsedKb;

    public TestCaseResult(boolean passed, String actualOutput, String errorMessage,
                          int executionTimeMs, int memoryUsedKb) {
        this.passed = passed;
        this.actualOutput = actualOutput;
        this.errorMessage = errorMessage;
        this.executionTimeMs = executionTimeMs;
        this.memoryUsedKb = memoryUsedKb;
    }

    public boolean isPassed() {
        return passed;
    }

    // Tên phương thức này phù hợp với logic chấm điểm trong DB của bạn (IsCorrect)
    public boolean isCorrect() {
        return passed;
    }

    public String getActualOutput() {
        return actualOutput;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getExecutionTimeMs() {
        return executionTimeMs;
    }

    public int getMemoryUsedKb() {
        return memoryUsedKb;
    }
}