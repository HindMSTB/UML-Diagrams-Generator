package org.mql.java.services;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.List;
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
        File[] fichiers = dossier.listFiles();
        if (fichiers != null) {
            Package packageActuel = null;

            for (File fichier : fichiers) {
                if (fichier.isFile() && fichier.getName().endsWith(".java")) {
                    if (packageActuel == null) {
                        packageActuel = new Package(nomDuPackage);
                    }
                    String nomElement = fichier.getName().replace(".java", "");
                    ajouterElementAuPackage(packageActuel, nomElement, fichier, nomDuPackage);
                } else if (fichier.isDirectory()) {
                    String sousPackage = nomDuPackage.isEmpty() ? fichier.getName() : nomDuPackage + "." + fichier.getName();
                    explorerLeDossier(fichier, sousPackage, projet);
                }
            }

            if (packageActuel != null && aDesElements(packageActuel)) {
                projet.addPackage(packageActuel);
            }
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
                Interface inter = new Interface(nomElement);
                for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
                    MethodInfos methodInfo = new MethodInfos(method);
                    inter.addMethod(methodInfo);
                }
                packageActuel.addInterface(inter);
            } else if (cls.isEnum()) {
                Enumeration enumeration = new Enumeration(nomElement);
                for (Object constant : cls.getEnumConstants()) {
                    enumeration.addConstant(constant.toString());
                }
                packageActuel.addEnumeration(enumeration);
            } else if (cls.isAnnotation()) {
                Annotation annotation = new Annotation(nomElement);

                for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
                    String attributeName = method.getName();
                    Object defaultValue = method.getDefaultValue();

                    String attributeDescription;
                    if (defaultValue != null) {
                        attributeDescription = attributeName + " : " + defaultValue.toString();
                    } else {
                        attributeDescription = attributeName + " ";
                    }

                    annotation.addAttribute(attributeDescription);
                }

                packageActuel.addAnnotation(annotation);
            } else {
                Classe classe = new Classe(nomElement);

                ajouterChamps(classe, cls);

                for (java.lang.reflect.Method method : cls.getDeclaredMethods()) {
                    if (!method.getName().startsWith("lambda$")) {
                        MethodInfos methodInfo = new MethodInfos(method);
                        classe.addMethod(methodInfo);
                    }
                }

                packageActuel.addClass(classe);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void ajouterChamps(Classe classe, Class<?> cls) {
        for (java.lang.reflect.Field field : cls.getDeclaredFields()) {
            String fieldName = field.getName();
            String fieldType = field.getType().getSimpleName();
            String fieldModifiers = Modifier.toString(field.getModifiers());
            classe.addField(fieldName + " : " + fieldType + " (" + fieldModifiers + ")");
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

        RelationExplorer relationExplorer = new RelationExplorer("org.mql.java");
        List<Relation> relations = relationExplorer.genererRelations(projet);

        System.out.println("=== Exploration du Projet ===");

        projet.getPackages().forEach(pkg -> {
            System.out.println("\nPackage: " + pkg.getName());

            pkg.getClasses().forEach(classe -> {
                System.out.println("  Classe: " + classe.getName());
                classe.getFields().forEach(field -> System.out.println("    Field: " + field));
                classe.getMethods().forEach(method -> System.out.println("    Method: " + method.toString()));
                classe.getRelationsHeritage().forEach(method -> System.out.println("  relation " + classe.getRelationsHeritage()));
            });

            pkg.getInterfaces().forEach(inter -> {
                System.out.println("  Interface: " + inter.getName());
                inter.getMethods().forEach(method -> System.out.println("    Method: " + method.toString()));
            });

            pkg.getAnnotations().forEach(annotation -> {
                System.out.println("  Annotation: " + annotation.getName());
                annotation.getAttributes().forEach(attr -> System.out.println("    Attribute: " + attr));
                annotation.getRelationsUtilisation().forEach(attr -> System.out.println("    relation: " + annotation.getRelationsUtilisation()));
            });

            pkg.getEnumerations().forEach(enumeration -> {
                System.out.println("  Enumeration: " + enumeration.getName());
                System.out.println( enumeration.getConstants());
            });
        });

        System.out.println("\n=== Relations dans le Projet ===");
        relations.forEach(rel -> System.out.println(
                rel.getSource() + " -> " + rel.getDestination() + " [" + rel.getType() + "]"));
    }
}
