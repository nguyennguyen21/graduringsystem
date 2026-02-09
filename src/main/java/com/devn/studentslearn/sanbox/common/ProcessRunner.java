package com.devn.studentslearn.sanbox.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessRunner {

    public static ProcessResult run(List<String> command, Path workDir, long timeoutSeconds) {
        try {
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(workDir.toFile());
            pb.redirectErrorStream(true);

            Process process = pb.start();
            StringBuilder output = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return new ProcessResult(false, "Timeout after " + timeoutSeconds + "s", null);
            }

            int exitCode = process.exitValue();
            return new ProcessResult(exitCode == 0, output.toString(), null);
        } catch (IOException | InterruptedException e) {
            return new ProcessResult(false, "", e.getMessage());
        }
    }

    public static class ProcessResult {
        private final boolean success;
        private final String output;
        private final String error;

        public ProcessResult(boolean success, String output, String error) {
            this.success = success;
            this.output = output;
            this.error = error;
        }

        public boolean isSuccess() { return success; }
        public String getOutput() { return output; }
        public String getError() { return error; }
    }
}