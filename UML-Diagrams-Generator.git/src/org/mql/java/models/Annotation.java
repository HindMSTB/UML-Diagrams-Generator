package org.mql.java.models;

import java.util.List;
import java.util.Vector;

public class Annotation {
	
    private String name;
    private List<String> attributes;

    public Annotation() {
        this.attributes = new Vector<>();
    }

    public Annotation(String name) {
        this.name = name;
        this.attributes = new Vector<>();
    }

    public void addAttribute(String attribute) {
        attributes.add(attribute);
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
