package org.mql.java.models;

import java.util.Vector;

public class Classe {
    private String name;
    private Vector<String> fields;
    private Vector<MethodInfos> methods;
    private Vector<Relation> relationsHeritage; 
    private Vector<Relation> relationImplementation;
    private Vector<Relation> relationUtilisation;
    private Vector<Relation> relationAgrégation;

    
    public Classe(String name) {
        this.name = name;
        this.fields = new Vector<>();
        this.methods = new Vector<>();
        this.relationsHeritage = new Vector<>();
        this.relationImplementation = new Vector<>();
        this.relationUtilisation = new Vector<>();
        this.relationAgrégation = new Vector<>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector<String> getFields() {
        return fields;
    }

    public void setFields(Vector<String> fields) {
        this.fields = fields;
    }

    public Vector<MethodInfos> getMethods() {
        return methods;
    }

    public void setMethods(Vector<MethodInfos> methods) {
        this.methods = methods;
    }

    public Vector<Relation> getRelationsHeritage() {
        return relationsHeritage;
    }

    public void setRelationsHeritage(Vector<Relation> relationsHeritage) {
        this.relationsHeritage = relationsHeritage;
    }

    public Vector<Relation> getRelationImplementation() {
        return relationImplementation;
    }

    public void setRelationImplementation(Vector<Relation> relationImplementation) {
        this.relationImplementation = relationImplementation;
    }

    public Vector<Relation> getRelationUtilisation() {
        return relationUtilisation;
    }

    public void setRelationUtilisation(Vector<Relation> relationUtilisation) {
        this.relationUtilisation = relationUtilisation;
    }

    public Vector<Relation> getRelationAgrégation() {
        return relationAgrégation;
    }

    public void setRelationAgrégation(Vector<Relation> relationAgrégation) {
        this.relationAgrégation = relationAgrégation;
    }

  
    public void addField(String field) {
        this.fields.add(field);
    }

    public void addMethod(MethodInfos method) {
        this.methods.add(method);
    }

   
    public void addRelationHeritage(Relation relation) {
        this.relationsHeritage.add(relation);
    }

    public void addRelationImplementation(Relation relation) {
        this.relationImplementation.add(relation);
    }

    public void addRelationUtilisation(Relation relation) {
        this.relationUtilisation.add(relation);
    }

    public void addRelationAgrégation(Relation relation) {
        this.relationAgrégation.add(relation);
    }
}
