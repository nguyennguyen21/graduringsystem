package com.devn.studentslearn.sanbox;

import com.devn.studentslearn.model.Codetestcase;

import java.util.List;
import java.util.Map;

public interface SandboxExecutor {
    SandboxResult execute(String userCode, List<Codetestcase> testCases, Map<String, Object> config);
}
