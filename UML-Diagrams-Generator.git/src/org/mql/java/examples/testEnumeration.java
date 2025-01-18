package org.mql.java.examples;

public enum testEnumeration {
    // Exemple d'éléments dans l'énumération
    ELEMENT1("Description 1"),
    ELEMENT2("Description 2"),
    ELEMENT3("Description 3");

    private final String description;

    // Constructeur pour l'énumération
    testEnumeration(String description) {
        this.description = description;
    }

    // Méthode pour récupérer la description
    public String getDescription() {
        return description;
    }
}
