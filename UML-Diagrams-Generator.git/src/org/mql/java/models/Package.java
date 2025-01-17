package org.mql.java.models;

import java.util.List;
import java.util.Vector;

public class Package {
	
    private String name;
    private List<Classe> classes;
    private List<Interface> interfaces;
    private List<Enumeration> enumerations;
    private List<Annotation> annotations;
    private List<Package> sousPackages;

    public List<Package> getSousPackages() {
		return sousPackages;
	}

	public void setSousPackages(List<Package> sousPackages) {
		this.sousPackages = sousPackages;
	}

	public Package() {
        this.classes = new Vector<>();
        this.interfaces = new Vector<>();
        this.enumerations = new Vector<>();
        this.annotations = new Vector<>();
    }

    public Package(String name) {
        this.name = name;
        this.classes = new Vector<>();
        this.interfaces = new Vector<>();
        this.enumerations = new Vector<>();
        this.annotations = new Vector<>();
    }

    public void addClass(Classe className) {
        classes.add(className);
    }

    public void addInterface(Interface iface) {
        interfaces.add(iface);
    }

    public void addEnumeration(Enumeration enumeration) {
        enumerations.add(enumeration);
    }

    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation);
    }

    public List<Classe> getClasses() {
        return classes;
    }

    public List<Interface> getInterfaces() {
        return interfaces;
    }

    public List<Enumeration> getEnumerations() {
        return enumerations;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public void setClasses(List<Classe> classes) {
		this.classes = classes;
	}

	public void setInterfaces(List<Interface> interfaces) {
		this.interfaces = interfaces;
	}

	public void setEnumerations(List<Enumeration> enumerations) {
		this.enumerations = enumerations;
	}

	public void setAnnotations(List<Annotation> annotations) {
		this.annotations = annotations;
	}

	
}
