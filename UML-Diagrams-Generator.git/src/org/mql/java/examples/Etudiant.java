package org.mql.java.examples;

@Profession("Student")
public class Etudiant implements Personne {
    private String nom;
    private String prenom;
    private NiveauScolaire niveau;
    private Matiere matierePreferee;

    public Etudiant(String nom, String prenom, NiveauScolaire niveau, Matiere matierePreferee) {
        this.nom = nom;
        this.prenom = prenom;
        this.niveau = niveau;
        this.matierePreferee = matierePreferee;
    }

    @Override
    public String getIdentifiant() {
        return "ETUDIANT-" + nom + "-" + prenom;
    }

    @Override
    public void afficherIdentite() {
        System.out.println("Nom : " + nom + ", Prénom : " + prenom + ", Niveau : " + niveau + ", Matière Préférée : " + matierePreferee);
    }
}