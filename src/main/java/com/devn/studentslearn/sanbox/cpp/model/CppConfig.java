package com.devn.studentslearn.sanbox.cpp.model;

public class CppConfig {
    private String functionName ;
    private String returnType ;
    private String parameters ;

    public CppConfig(String functionName, String returnType, String parameters) {
        this.functionName = functionName;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    // Getters
    public String getFunctionName() { return functionName; }
    public String getReturnType() { return returnType; }
    public String getParameters() { return parameters; }
}