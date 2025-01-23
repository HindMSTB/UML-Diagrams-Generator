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
    
    public void addMethod(MethodInfos method) {
        methods.add(method);
    }

	public Vector<MethodInfos> getMethods() {
		return methods;
	}

	public void setMethods(Vector<MethodInfos> methods) {
		this.methods = methods;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAttributes(Vector<String> attributes) {
		this.attributes = attributes;
	}

	public void setRelationsUtilisation(Vector<Relation> relationsUtilisation) {
		this.relationsUtilisation = relationsUtilisation;
	}

	public void setRelationAgrégation(Vector<Relation> relationAgrégation) {
		this.relationAgrégation = relationAgrégation;
	}
	
	
}

