package com.devn.studentslearn.dto.reponse;

import com.devn.studentslearn.model.Codesubmission;


import java.util.List;

public class CodeSubmissionResponse {
    private Codesubmission submission;
    private List<CppCheckWarning> staticAnalysisWarnings;

    public CodeSubmissionResponse(Codesubmission submission, List<CppCheckWarning> warnings) {
        this.submission = submission;
        this.staticAnalysisWarnings = warnings;
    }

    public Codesubmission getSubmission() { return submission; }
    public List<CppCheckWarning> getStaticAnalysisWarnings() { return staticAnalysisWarnings; }

    public static class CppCheckWarning {
        private Integer lineNumber;
        private String message;
        private String severity;

        public CppCheckWarning(Integer lineNumber, String message, String severity) {
            this.lineNumber = lineNumber;
            this.message = message;
            this.severity = severity;
        }

        public Integer getLineNumber() { return lineNumber; }
        public String getMessage() { return message; }
        public String getSeverity() { return severity; }
    }
}