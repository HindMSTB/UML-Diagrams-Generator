package org.mql.java.models;

import java.util.List;
import java.util.Vector;

public class Enumeration {
    private String name;
    private List<String> constants;
    private Vector<Relation> relationsUtilisation; 
    private Vector<MethodInfos> methods;

    public Enumeration() {
        this.constants = new Vector<>();
        this.relationsUtilisation = new Vector<>();
        this.methods = new Vector<>();
    }

    public Enumeration(String name) {
        this.name = name;
        this.constants = new Vector<>();
    }

    public void addConstant(String constant) {
        constants.add(constant);
    }

    public List<String> getConstants() {
        return constants;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addRelationUtilisation(Relation relation) {
        relationsUtilisation.add(relation);
    }
    
    public Vector<MethodInfos> getMethods() {
        return methods;
    }

    public void setMethods(Vector<MethodInfos> methods) {
        this.methods = methods;
    }
}
