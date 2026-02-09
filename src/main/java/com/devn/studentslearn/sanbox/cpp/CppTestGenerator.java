package com.devn.studentslearn.sanbox.cpp;

import com.devn.studentslearn.model.Codetestcase;
import com.devn.studentslearn.sanbox.cpp.model.CppConfig;

import java.util.*;
import java.util.stream.Collectors;

public class CppTestGenerator {

    public static CppConfig parseConfigFromSignature(String rawSignature) {
        if (rawSignature == null || rawSignature.trim().isEmpty()) {
            throw new IllegalArgumentException("Function signature is empty");
        }

        String sig = rawSignature.trim();
        if (sig.endsWith(";")) {
            sig = sig.substring(0, sig.length() - 1);
        }

        int open = sig.indexOf('(');
        int close = sig.lastIndexOf(')');

        if (open == -1 || close == -1 || open >= close) {
            throw new IllegalArgumentException("Invalid signature: " + rawSignature);
        }

        String retAndName = sig.substring(0, open).trim();
        String params = sig.substring(open + 1, close).trim();

        int lastSpace = retAndName.lastIndexOf(' ');
        if (lastSpace == -1) {
            throw new IllegalArgumentException("Cannot parse function name: " + retAndName);
        }

        String returnType = retAndName.substring(0, lastSpace).trim();
        String funcName = retAndName.substring(lastSpace + 1).trim();

        return new CppConfig(funcName, returnType, params);
    }

    public static String generateTestFile(List<Codetestcase> testCases, CppConfig config) {
        StringBuilder sb = new StringBuilder();
        sb.append("#include <gtest/gtest.h>\n\n");
        sb.append("#include <iostream>\n");
        sb.append("#include <vector>\n");
        sb.append("#include <string>\n");
        sb.append("#include <utility>\n");
        sb.append("#include <tuple>\n");
        sb.append("#include <memory>\n");
        sb.append("#include <cstddef>\n");
        sb.append("#include <gtest/gtest.h>\n\n");

        for (int i = 0; i < testCases.size(); i++) {
            Codetestcase tc = testCases.get(i);
            String input = Optional.ofNullable(tc.getInput()).orElse("").trim();
            String expected = Optional.ofNullable(tc.getExpectedOutput()).orElse("").trim();

            sb.append("TEST(UserFunctionTest, TestCase_").append(i).append(") {\n");

            String call = buildFunctionCall(config, input);
            sb.append("    auto result = ").append(call).append(";\n");

            String expectedCpp = toCppLiteral(config.getReturnType(), expected);
            sb.append("    EXPECT_EQ(result, ").append(expectedCpp).append(");\n");

            sb.append("}\n\n");
        }

        return sb.toString();
    }

    private static String buildFunctionCall(CppConfig config, String input) {
        String func = config.getFunctionName();
        String params = config.getParameters();

        if (params == null || params.trim().isEmpty()) {
            return func + "()";
        }

        String[] parts = params.split(",");
        if (parts.length != 2) {
            return buildGeneralCall(func, parts, input);
        }

        String arrParam = parts[0].trim();
        String sizeParam = parts[1].trim();

        boolean isArray = arrParam.contains("[]") || (arrParam.contains("*") && !arrParam.startsWith("char"));
        boolean isSize = sizeParam.startsWith("int");

        if (isArray && isSize) {
            return buildArrayCall(config, func, arrParam, input);
        }

        return buildGeneralCall(func, parts, input);
    }

    private static String extractBaseType(String arrParam) {
        String clean = arrParam.trim();
        if (clean.contains("[")) {
            clean = clean.substring(0, clean.indexOf('['));
        } else if (clean.contains("*")) {
            clean = clean.substring(0, clean.indexOf('*'));
        }
        String[] tokens = clean.split("\\s+");
        for (String token : tokens) {
            if (!token.isEmpty()) {
                return token;
            }
        }
        return "int";
    }

    private static String buildArrayCall(CppConfig config, String func, String arrParam, String input) {
        List<String> tokens = Arrays.stream(input.split("\\s+"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        int n;
        List<String> elements;

        if (tokens.isEmpty()) {
            return func + "(nullptr, 0)";
        } else {
            try {
                n = Integer.parseInt(tokens.get(0));
                if (tokens.size() != n + 1) {
                    return func + "()";
                }
                elements = tokens.subList(1, n + 1);
            } catch (Exception e) {
                return func + "()";
            }
        }

        String baseType = extractBaseType(arrParam);
        String init = String.join(", ", elements);
        return "([]() { " + baseType + " _arr[] = {" + init + "}; return " + func + "(_arr, " + n + "); }())";
    }

    private static String buildGeneralCall(String func, String[] paramDecls, String input) {
        List<String> args = new ArrayList<>();
        String[] values = input.split("\\s+");

        if (values.length != paramDecls.length) {
            return func + "()";
        }

        for (int i = 0; i < paramDecls.length; i++) {
            String decl = paramDecls[i].trim();
            String val = values[i];

            if (decl.contains("string") || decl.contains("char*")) {
                args.add("\"" + escape(val) + "\"");
            } else if (decl.contains("bool")) {
                args.add(val.equalsIgnoreCase("true") || val.equals("1") ? "true" : "false");
            } else if (isNumeric(val)) {
                args.add(val);
            } else {
                args.add(val);
            }
        }

        return func + "(" + String.join(", ", args) + ")";
    }

    private static String toCppLiteral(String returnType, String expected) {
        if (expected.isEmpty()) return "false";
        if (returnType.contains("bool")) {
            return expected.equalsIgnoreCase("true") || expected.equals("1") ? "true" : "false";
        } else if (returnType.contains("string")) {
            return "\"" + escape(expected) + "\"";
        } else {
            return expected;
        }
    }

    private static boolean isNumeric(String s) {
        try { Double.parseDouble(s); return true; } catch (Exception e) { return false; }
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}