// File: com/devn/studentslearn/sanbox/cpp/model/GTestResult.java

package com.devn.studentslearn.sanbox.cpp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GTestResult {

    private List<TestSuiteContainer> testsuites;

    @JsonIgnoreProperties(ignoreUnknown = true) // ← THÊM DÒNG NÀY
    public static class TestSuiteContainer {
        private String name; // ← (tùy chọn) bạn có thể thêm nếu cần dùng
        private List<TestCase> testsuite;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<TestCase> getTestsuite() {
            return testsuite;
        }

        public void setTestsuite(List<TestCase> testsuite) {
            this.testsuite = testsuite;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true) // ← THÊM DÒNG NÀY (tốt cho tương lai)
    public static class TestCase {
        private String name;
        private String result;
        private List<Failure> failures;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public List<Failure> getFailures() {
            return failures;
        }

        public void setFailures(List<Failure> failures) {
            this.failures = failures;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true) // ← THÊM DÒNG NÀY
    public static class Failure {
        private String failure;

        public String getFailure() {
            return failure;
        }

        public void setFailure(String failure) {
            this.failure = failure;
        }
    }

    public List<TestSuiteContainer> getTestsuites() {
        return testsuites;
    }

    public void setTestsuites(List<TestSuiteContainer> testsuites) {
        this.testsuites = testsuites;
    }
}