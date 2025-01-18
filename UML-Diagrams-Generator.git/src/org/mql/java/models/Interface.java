package org.mql.java.models;
import java.util.Vector;

public class Interface {
	
    private String name;
    private Vector<MethodInfos> methods;
    private Vector<Relation> relationsHeritage;  // Inheritance relations
    private Vector<Relation> relationUtilisation; // Usage relations

    public Interface(String name) {
        this.name = name;
        this.methods = new Vector<>();
        this.relationsHeritage = new Vector<>();
        this.relationUtilisation = new Vector<>();
    }

    public void addMethod(MethodInfos method) {
        methods.add(method);
    }

    public void addRelationHeritage(Relation relation) {
        relationsHeritage.add(relation);
    }

    public void addRelationUtilisation(Relation relation) {
        relationUtilisation.add(relation);
    }

    public Vector<Relation> getRelationsHeritage() {
        return relationsHeritage;
    }

    public Vector<Relation> getRelationUtilisation() {
        return relationUtilisation;
    }

    public String getName() {
        return name;
    }

    public Vector<MethodInfos> getMethods() {
        return methods;
    }
}
