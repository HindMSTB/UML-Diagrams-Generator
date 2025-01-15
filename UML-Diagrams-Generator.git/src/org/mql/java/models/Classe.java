package org.mql.java.models;

import java.util.List;
import java.util.Vector;

public class Classe {
	
    private String name;
    private List<String> fields;
    private List<String> methods;

    public Classe() {
        this.fields = new Vector<>();
        this.methods = new Vector<>();
    }

    public Classe(String name) {
        this.name = name;
        this.fields = new Vector<>();
        this.methods = new Vector<>();
    }

    public void addField(String field) {
        fields.add(field);
    }

    public void addMethod(String method) {
        methods.add(method);
    }

    public List<String> getFields() {
        return fields;
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
