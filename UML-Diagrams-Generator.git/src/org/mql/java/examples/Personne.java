package org.mql.java.examples;

public class Personne {

    public Personne() {
        // TODO Auto-generated constructor stub
    }

    @testAnnotation1(value = "John Doe", number = 30)
    public void afficherNom() {
        // Méthode pour afficher le nom
        System.out.println("Nom : John Doe");
    }
}
