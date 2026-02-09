
package com.devn.studentslearn.sanbox;

import java.util.List;

public class SandboxResult {
    private boolean buildSuccess;
    private String buildOutput;
    private List<TestCaseResult> testCaseResults;


    public SandboxResult(boolean buildSuccess, String buildOutput, List<TestCaseResult> testCaseResults) {
        this.buildSuccess = buildSuccess;
        this.buildOutput = buildOutput;
        this.testCaseResults = testCaseResults != null ? testCaseResults : List.of();
    }

    // Getters và setters
    public boolean isBuildSuccess() {
        return buildSuccess;
    }

    public void setBuildSuccess(boolean buildSuccess) {
        this.buildSuccess = buildSuccess;
    }

    public List<TestCaseResult> getTestCaseResults() {
        return testCaseResults;
    }

    public void setTestCaseResults(List<TestCaseResult> testCaseResults) {
        this.testCaseResults = testCaseResults;
    }

    public String getBuildOutput() {
        return buildOutput;
    }

    public void setBuildOutput(String buildOutput) {
        this.buildOutput = buildOutput;
    }
}