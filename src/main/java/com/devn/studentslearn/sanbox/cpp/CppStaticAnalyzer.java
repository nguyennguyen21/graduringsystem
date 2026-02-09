package com.devn.studentslearn.sanbox.cpp;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CppStaticAnalyzer {

    public CppCheckResult analyze(String sourceFilePath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "cppcheck",
                "--enable=warning,performance,portability,style",
                "--suppress=missingIncludeSystem",
                "--quiet",
                "--template={file}:{line}:{severity}:{message}",
                sourceFilePath
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        String output = new String(process.getInputStream().readAllBytes());
        process.waitFor();

        return new CppCheckResult(parseOutput(output), output.trim());
    }

    private List<CppCheckIssue> parseOutput(String output) {
        List<CppCheckIssue> issues = new ArrayList<>();
        if (output == null || output.trim().isEmpty()) return issues;

        for (String line : output.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.split(":", 4);
            if (parts.length < 4) continue;

            issues.add(new CppCheckIssue(parts[1], parts[2], parts[3]));
        }
        return issues;
    }

    public static class CppCheckIssue {
        private final String lineNumber;
        private final String severity;
        private final String message;

        public CppCheckIssue(String lineNumber, String severity, String message) {
            this.lineNumber = lineNumber;
            this.severity = severity;
            this.message = message;
        }

        public String getLineNumber() { return lineNumber; }
        public String getSeverity() { return severity; }
        public String getMessage() { return message; }
    }

    public static class CppCheckResult {
        private final List<CppCheckIssue> issues;
        private final String rawOutput;

        public CppCheckResult(List<CppCheckIssue> issues, String rawOutput) {
            this.issues = issues;
            this.rawOutput = rawOutput;
        }

        public List<CppCheckIssue> getIssues() { return issues; }
        public String getRawOutput() { return rawOutput; }
    }
}