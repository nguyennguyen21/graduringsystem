// src/main/java/com/devn/studentslearn/sanbox/common/FileUtil.java
package com.devn.studentslearn.sanbox.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileUtil {

    public static String readResource(String resourcePath) {
        try {
            var stream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(resourcePath);
            if (stream == null) {
                System.err.println("❌ RESOURCE KHÔNG TỒN TẠI: " + resourcePath);
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("🔥 LỖI KHI ĐỌC RESOURCE: " + resourcePath);
            e.printStackTrace();
            throw new RuntimeException("Failed to read resource: " + resourcePath, e);
        }
    }

    public static void writeStringToFile(String content, Path filePath) throws IOException {
        // Tạo thư mục cha nếu chưa tồn tại
        Files.createDirectories(filePath.getParent());
        // Ghi nội dung vào file (UTF-8, ghi đè)
        Files.write(filePath, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}