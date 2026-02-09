// src/main/java/com/devn/studentslearn/sanbox/cpp/CppSandbox.java
package com.devn.studentslearn.sanbox.cpp;

import com.devn.studentslearn.model.Codetestcase;
import com.devn.studentslearn.sanbox.SandboxExecutor;
import com.devn.studentslearn.sanbox.SandboxResult;
import com.devn.studentslearn.sanbox.TestCaseResult;
import com.devn.studentslearn.sanbox.common.FileUtil;
import com.devn.studentslearn.sanbox.common.ProcessRunner;
import com.devn.studentslearn.sanbox.common.ResourceCleaner;
import com.devn.studentslearn.sanbox.cpp.model.CppConfig;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class CppSandbox implements SandboxExecutor {

    private static final Logger log = LoggerFactory.getLogger(CppSandbox.class);
    private static final String TEMPLATE_DIR = "sandbox-templates/cpp/";
    private static final long TIMEOUT_SEC = 30;

    @Override
    public SandboxResult execute(String userCode, List<Codetestcase> testCases, Map<String, Object> config) {
        log.info(" Bắt đầu thực thi C++ sandbox cho {} test case", testCases.size());

        CppConfig cppConfig = buildCppConfig(config);
        if (cppConfig == null) {
            String msg = "Không thể tạo CppConfig từ functionSignatures";
            log.error(msg);
            return createErrorResult(testCases, msg);
        }

        Path tempDir = Paths.get("C:\\tmp\\cpp_gtest_" + UUID.randomUUID().toString().substring(0, 8));
        log.debug(" Thư mục tạm: {}", tempDir);

        try {
            Files.createDirectories(tempDir);
            log.debug(" Đã tạo thư mục tạm");

            writeTemplateFile(tempDir, "CMakeLists.txt");
            log.debug(" Đã ghi CMakeLists.txt");

            String testContent = CppTestGenerator.generateTestFile(testCases, cppConfig);
            String fullContent = userCode + "\n\n" + testContent;
            FileUtil.writeStringToFile(fullContent, tempDir.resolve("unit_tests.cpp"));
            log.debug(" Đã ghi unit_tests.cpp");

            // === Cấu hình CMake ===
            log.info(" Chạy CMake configure...");
            var cmake = ProcessRunner.run(
                    List.of("cmake", "-S", ".", "-B", "build", "-G", "MinGW Makefiles"),
                    tempDir,
                    TIMEOUT_SEC
            );
            if (!cmake.isSuccess()) {
                String msg = "CMake configure failed:\n" + cmake.getOutput();
                log.error(msg);
                return createErrorResult(testCases, msg);
            }
            log.info(" CMake configure thành công");

            // === Build ===
            log.info(" Đang build runner.exe...");
            var build = ProcessRunner.run(
                    List.of("cmake", "--build", "build"),
                    tempDir,
                    TIMEOUT_SEC
            );
            if (!build.isSuccess()) {
                String msg = "Build failed:\n" + build.getOutput();
                log.error(msg);
                return createErrorResult(testCases, msg);
            }
            log.info(" Build thành công");

            // === Tìm runner.exe ===
            Path runnerExe = tempDir.resolve("build").resolve("runner.exe");
            if (!Files.exists(runnerExe)) {
                String msg = "Executable not found at: " + runnerExe.toAbsolutePath();
                log.error(msg);
                return createErrorResult(testCases, msg);
            }
            log.debug(" Tìm thấy runner.exe");

            // === Chạy test ===
            Path jsonOutput = tempDir.resolve("test_results.json");
            String escapedJsonPath = jsonOutput.toAbsolutePath().toString().replace("\\", "\\\\");
            log.info(" Chạy Google Test, output JSON: {}", jsonOutput);

            ProcessRunner.run(
                    List.of(runnerExe.toAbsolutePath().toString(), "--gtest_output=json:" + escapedJsonPath),
                    tempDir,
                    TIMEOUT_SEC
            );

            // === Kiểm tra kết quả ===
            if (Files.exists(jsonOutput)) {
                log.debug(" Đã tạo file JSON kết quả");
                List<TestCaseResult> results = GoogleTestParser.parse(jsonOutput, testCases.size());
                log.info(" Đã phân tích {} kết quả test", results.size());
                return new SandboxResult(true, "", results);
            } else {
                String msg = "Google Test did not produce output. Executable may have crashed.";
                log.error(msg);
                return createErrorResult(testCases, msg);
            }

        } catch (Exception e) {
            log.error(" LỖI NẶNG TRONG CPP SANDBOX", e);
            return createErrorResult(testCases, "System error: " + e.getMessage());
        } finally {
            // Bật lại khi đã ổn định
            // ResourceCleaner.deleteDirectory(tempDir);
            log.debug("🗑 Thư mục tạm sẽ được xóa thủ công để debug: {}", tempDir);
        }
    }


    private CppConfig buildCppConfig(Map<String, Object> config) {
        @SuppressWarnings("unchecked")
        List<String> signatures = (List<String>) config.get("functionSignatures");
        if (signatures == null || signatures.isEmpty()) {
            return null;
        }

        String rawSig = signatures.get(0);
        return CppTestGenerator.parseConfigFromSignature(rawSig);
    }

    private void writeTemplateFile(Path dir, String filename) throws Exception {
        log.debug(" Đang đọc resource: {}", TEMPLATE_DIR + filename);
        String content = FileUtil.readResource(TEMPLATE_DIR + filename);
        FileUtil.writeStringToFile(content, dir.resolve(filename));
        log.debug(" Đã ghi file: {}", filename);
    }

    private SandboxResult createErrorResult(List<Codetestcase> testCases, String errorMsg) {
        log.warn(" Tạo kết quả lỗi cho tất cả test case: {}", errorMsg);
        List<TestCaseResult> results = new ArrayList<>();
        for (Codetestcase tc : testCases) {
            results.add(new TestCaseResult(false, "", errorMsg, 0, 0));
        }
        return new SandboxResult(false, errorMsg, results);
    }
}