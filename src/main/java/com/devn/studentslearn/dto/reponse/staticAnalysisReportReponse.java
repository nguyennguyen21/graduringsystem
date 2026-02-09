package com.devn.studentslearn.dto.reponse;

import java.util.List;

public class staticAnalysisReportReponse {
    private List<Warning> warnings;

    public staticAnalysisReportReponse(List<Warning> warnings) {
        this.warnings = warnings;
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    public static class Warning {
        private Integer lineNumber;
        private String message;
        private String severity;

        public Warning(Integer lineNumber, String message, String severity) {
            this.lineNumber = lineNumber;
            this.message = message;
            this.severity = severity;
        }

        public Integer getLineNumber() {
            return lineNumber;
        }

        public String getMessage() {
            return message;
        }

        public String getSeverity() {
            return severity;
        }
    }
}
