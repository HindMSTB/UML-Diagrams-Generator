package org.mql.java.examples;

@Profession("Teacher")
public class Enseignant implements Personne {
    private String nom;
    private String prenom;
    private Matiere matiereEnseignee;
    private NiveauScolaire niveauEnseigne;

    public Enseignant(String nom, String prenom, Matiere matiereEnseignee, NiveauScolaire niveauEnseigne) {
        this.nom = nom;
        this.prenom = prenom;
        this.matiereEnseignee = matiereEnseignee;
        this.niveauEnseigne = niveauEnseigne;
    }

    @Override
    public String getIdentifiant() {
        return "ENSEIGNANT-" + nom + "-" + prenom;
    }

    @Override
    public void afficherIdentite() {
        System.out.println("Nom : " + nom + ", Prénom : " + prenom + ", Matière Enseignée : " + matiereEnseignee + ", Niveau Enseigné : " + niveauEnseigne);
    }
}