package com.devn.studentslearn.sanbox.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class ProcessRunner {
    private static final Logger log = LoggerFactory.getLogger(ProcessRunner.class);

    public static ProcessResult runWithEnv(List<String> command, Path workingDir,
                                           long timeoutSeconds, Map<String, String> env) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(workingDir.toFile());
        if (env != null) {
            pb.environment().putAll(env);
        }

        log.debug("Chạy lệnh: {} trong {}", String.join(" ", command), workingDir);
        Process process = pb.start();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> outputFuture = executor.submit(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                return reader.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                return "Lỗi đọc output: " + e.getMessage();
            }
        });

        boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
        executor.shutdown();

        String output = "";
        try {
            output = outputFuture.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            output = "Không đọc được toàn bộ output";
        }

        if (!finished) {
            process.destroyForcibly();
            return new ProcessResult(false, "Timeout sau " + timeoutSeconds + " giây", -1);
        }

        return new ProcessResult(process.exitValue() == 0, output, process.exitValue());
    }

    public static ProcessResult run(List<String> command, Path workingDir, long timeoutSeconds)
            throws IOException, InterruptedException {
        return runWithEnv(command, workingDir, timeoutSeconds, null);
    }

    public static class ProcessResult {
        private final boolean success;
        private final String output;
        private final int exitCode;

        public ProcessResult(boolean success, String output, int exitCode) {
            this.success = success;
            this.output = output != null ? output : "";
            this.exitCode = exitCode;
        }

        public boolean isSuccess() { return success; }
        public String getOutput() { return output; }
        public int getExitCode() { return exitCode; }
    }
}