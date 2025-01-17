package org.mql.java.services;

import java.io.File;
import org.mql.java.models.*;
import org.mql.java.models.Package;

public class ProjectExplorer {

    private final Project projet;

    public ProjectExplorer(String cheminDuProjet) {
        this.projet = new Project(""); 
        explorerLeProjet(cheminDuProjet, projet);
    }

    private void explorerLeProjet(String cheminDuProjet, Project projet) {
        File racine = new File(cheminDuProjet);
        if (racine.exists() && racine.isDirectory()) {
            explorerLeDossier(racine, "", projet);
        } 
    }

    private void explorerLeDossier(File dossier, String nomDuPackage, Project projet) {
        File[] fichiers = dossier.listFiles(File::isFile);
        File[] sousDossiers = dossier.listFiles(File::isDirectory);

        Package packageActuel = null;
        if (fichiers != null) {
            for (File fichier : fichiers) {
                if (fichier.getName().endsWith(".java")) {
                    if (packageActuel == null) {
                        packageActuel = new Package(nomDuPackage);
                    }
                    String nomElement = fichier.getName().replace(".java", "");
                    ajouterElementAuPackage(packageActuel, nomElement, fichier, nomDuPackage);
                }
            }
        }

        if (sousDossiers != null) {
            for (File sousDossier : sousDossiers) {
                String sousPackage;
                
                if (nomDuPackage.isEmpty()) {
                    sousPackage = sousDossier.getName();
                } else {
                    sousPackage = nomDuPackage + "." + sousDossier.getName();
                }

                explorerLeDossier(sousDossier, sousPackage, projet);
            }
        }

        if (packageActuel != null && aDesElements(packageActuel)) {
            projet.addPackage(packageActuel);
        }
    }

    private void ajouterElementAuPackage(Package packageActuel, String nomElement, File fichier, String nomDuPackage) {
        try {
            String nomDeClasse;
            if (nomDuPackage.isEmpty()) {
                nomDeClasse = nomElement;
            } else {
                nomDeClasse = nomDuPackage + "." + nomElement;
            }

            Class<?> cls = Class.forName(nomDeClasse);

            if (cls.isInterface() && !cls.isAnnotation()) {
                packageActuel.addInterface(new Interface(nomElement));
            } else if (cls.isEnum()) {
                packageActuel.addEnumeration(new Enumeration(nomElement));
            } else if (cls.isAnnotation()) {
                packageActuel.addAnnotation(new Annotation(nomElement));
            } else {
                packageActuel.addClass(new Classe(nomElement));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean aDesElements(Package pkg) {
        return !pkg.getClasses().isEmpty() ||
               !pkg.getInterfaces().isEmpty() ||
               !pkg.getEnumerations().isEmpty() ||
               !pkg.getAnnotations().isEmpty();
    }

    public Project getProject() {
        return projet;
    }

    public static void main(String[] args) {
        String cheminDuProjet = "src";
        ProjectExplorer explorateur = new ProjectExplorer(cheminDuProjet);

        Project projet = explorateur.getProject();
        System.out.println("Project:");

        projet.getPackages().forEach(pkg -> {
            System.out.println("Package: " + pkg.getName());
            pkg.getClasses().forEach(cls -> System.out.println("  Classes: " + cls.getName()));
            pkg.getInterfaces().forEach(iface -> System.out.println("  Interfaces: " + iface.getName()));
            pkg.getEnumerations().forEach(enumItem -> System.out.println("  Enumerations: " + enumItem.getName()));
            pkg.getAnnotations().forEach(annotation -> System.out.println("  Annotations: " + annotation.getName()));
        });
    }
}
