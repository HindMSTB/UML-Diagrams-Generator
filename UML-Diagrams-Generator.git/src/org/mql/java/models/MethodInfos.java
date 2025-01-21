
    package org.mql.java.models;

    import java.lang.reflect.Method;
    import java.lang.reflect.Modifier;
    import java.util.Arrays;
    import java.util.stream.Collectors;

    public class MethodInfos {

        private String name;
        private String returnType;
        private String methodVisibility;
        private String parameters;

        public MethodInfos(Method method) {
            name = method.getName();
            returnType = method.getReturnType().getSimpleName();
            methodVisibility = getVisibility(method);
            parameters = "(" + 
                Arrays.stream(method.getParameterTypes())
                      .map(Class::getSimpleName)
                      .collect(Collectors.joining(", ")) + 
                ")";
        }

        private String getVisibility(Method method) {
            int modifiers = method.getModifiers();
            
            if (Modifier.isPrivate(modifiers)) {
                return "private";
            } else if (Modifier.isPublic(modifiers)) {
                return "public";
            } else if (Modifier.isProtected(modifiers)) {
                return "protected";
            } else if (method.isDefault()) {  
                return "default";
            }
            return ""; 
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
        return  methodVisibility  +name + " : " + returnType + " " + " " + parameters;
    }
}

