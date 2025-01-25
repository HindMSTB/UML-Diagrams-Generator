package org.mql.java.models;

import java.util.List;
import java.util.Vector;

public class Project {
	
    private String name;
    private List<Package> packages;

    public Project() {
        this.packages = new Vector<>();
    }

    public Project(String name) {
        this.name = name;
        this.packages = new Vector<>();
    }

    public void addPackage(Package pkg) {
        packages.add(pkg);
    }

    public List<Package> getPackages() {
        return packages;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
 

}
