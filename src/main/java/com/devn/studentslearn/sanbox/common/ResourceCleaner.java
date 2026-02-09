package com.devn.studentslearn.sanbox.common;

import java.io.IOException;
import java.nio.file.*;

public class ResourceCleaner {
    public static void deleteDirectory(Path path) {
        if (!Files.exists(path)) return;
        try {
            Files.walk(path)
                    .sorted(java.util.Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(java.io.File::delete);
        } catch (IOException ignored) {}
    }
}