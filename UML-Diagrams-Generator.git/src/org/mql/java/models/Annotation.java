package org.mql.java.models;
import java.util.Vector;

public class Annotation {
	
    private String name;
    private Vector<String> attributes;
    private Vector<Relation> relationsUtilisation; 
    private Vector<Relation> relationAgrégation; 
    private Vector<MethodInfos> methods;
    

    public Annotation(String name) {
        this.name = name;
        this.attributes = new Vector<>();
        this.relationsUtilisation = new Vector<>();
        this.relationAgrégation = new Vector<>();
    }

    public void addAttribute(String attribute) {
        attributes.add(attribute);
    }

    public void addRelationUtilisation(Relation relation) {
        relationsUtilisation.add(relation);
    }

    public void addRelationAgrégation(Relation relation) {
        relationAgrégation.add(relation);
    }

    public Vector<Relation> getRelationsUtilisation() {
        return relationsUtilisation;
    }

    public Vector<Relation> getRelationAgrégation() {
        return relationAgrégation;
    }

    public String getName() {
        return name;
    }

    public Vector<String> getAttributes() {
        return attributes;
    }
}
