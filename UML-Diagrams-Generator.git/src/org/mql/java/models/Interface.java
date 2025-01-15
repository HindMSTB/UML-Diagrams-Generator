package org.mql.java.models;

import java.util.List;
import java.util.Vector;

public class Interface {
	
    private String name;
    private List<String> methods;

    public Interface() {
        this.methods = new Vector<>();
    }

    public Interface(String name) {
        this.name = name;
        this.methods = new Vector<>();
    }

    public void addMethod(String method) {
        methods.add(method);
    }

    public List<String> getMethods() {
        return methods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
