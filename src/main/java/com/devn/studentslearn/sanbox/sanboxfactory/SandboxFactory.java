// src/main/java/com/devn/coderlearning/sandbox/sandboxfactory/SandboxFactory.java
package com.devn.studentslearn.sanbox.sanboxfactory;

import com.devn.studentslearn.sanbox.SandboxExecutor;
import com.devn.studentslearn.sanbox.cpp.CppSandbox;
import org.springframework.stereotype.Service;

@Service
public class SandboxFactory {
    private final CppSandbox cppSandbox;

    public SandboxFactory(CppSandbox cppSandbox) {
        this.cppSandbox = cppSandbox;
    }

    public SandboxExecutor getExecutor(String languageId) {
        if ("cpp".equals(languageId) || "4".equals(languageId)) {
            return cppSandbox;
        }
        throw new IllegalArgumentException("Unsupported language: " + languageId);
    }
}