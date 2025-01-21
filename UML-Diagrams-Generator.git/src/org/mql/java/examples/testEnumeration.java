package org.mql.java.examples;

public enum testEnumeration {
   
    ELEMENT1("Description 1"),
    ELEMENT2("Description 2"),
    ELEMENT3("Description 3");

    private final String description;

    testEnumeration(String description) {
        this.description = description;
    }

    
    public String getDescription() {
        return description;
    }
}
