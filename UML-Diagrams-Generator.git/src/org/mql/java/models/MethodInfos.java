package org.mql.java.models;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MethodInfos {

    private String name;
    private String returnType;
    private String methodVisibility; // Store visibility as a string (e.g., "public", "private", "protected")
    private String parameters;

    

    public MethodInfos(Method method) {
        name = method.getName();
        returnType = method.getReturnType().getSimpleName();
        methodVisibility = decodeModifier(method.getModifiers());
        parameters = "(" + 
            Arrays.stream(method.getParameterTypes())
                  .map(Class::getSimpleName)
                  .collect(Collectors.joining(", ")) + 
            ")";
    }

    // Decodes the modifier to a UML-style visibility string
    private String decodeModifier(int modifier) {
        String modifiers = Modifier.toString(modifier);
        if (modifiers.contains("public")) return "+";
        else if (modifiers.contains("private")) return "-";
        else if (modifiers.contains("protected")) return "#";
        else return "~";
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getMethodVisibility() {
        return methodVisibility;
    }

    public void setMethodVisibility(String methodVisibility) {
        this.methodVisibility = methodVisibility;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return name + " : " + returnType + " " + methodVisibility + " " + parameters;
    }
}

