package org.mql.java.examples;

public class Personne {

    public Personne() {
        // TODO Auto-generated constructor stub
    }

    @testAnnotation1(value = "John Doe", number = 30)
    public void afficherNom() {
        
        System.out.println("Nom : John Doe");
    }
}
