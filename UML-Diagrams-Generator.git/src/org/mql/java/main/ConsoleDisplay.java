package org.mql.java.main;

import java.io.File;
import java.util.List;

import org.mql.java.models.Annotation;
import org.mql.java.models.Classe;
import org.mql.java.models.Enumeration;
import org.mql.java.models.Interface;
import org.mql.java.models.MethodInfos;
import org.mql.java.models.Package;
import org.mql.java.models.Project;
import org.mql.java.models.Relation;
import org.mql.java.services.ProjectExplorer;
import org.mql.java.services.RelationExplorer;
import org.mql.java.xml.XMLFileGenerator;
import org.mql.java.xml.XMLParser;

public class ConsoleDisplay {

    private String projectPath;
    private String xmlFilePath;

    public ConsoleDisplay(String projectPath, String xmlFilePath) {
        this.projectPath = projectPath;
        this.xmlFilePath = xmlFilePath;
        exploreProject();
        generateXMLFile();
        parserXMLFile();
    }

    private void exploreProject() {
        String cheminDuProjet = "src";
        ProjectExplorer explorateur = new ProjectExplorer(cheminDuProjet);
        Project projet = explorateur.getProject();

        System.out.println("=== Exploration du Projet ===");

        projet.getPackages().forEach(pkg -> {
            System.out.println("\nPackage: " + pkg.getName());

            RelationExplorer relationExplorer = new RelationExplorer(pkg.getName());
            List<Relation> relations = relationExplorer.genererRelations(projet);

            pkg.getClasses().forEach(classe -> {
                System.out.println("  Classe: " + classe.getName());
                classe.getMethods().forEach(method -> 
                    System.out.println("    Méthode: " + method.toString())
                );
                System.out.println("    Relations :");
                relations.stream()
                    .filter(relation -> relation.getSource().equals(classe.getName()))
                    .forEach(relation -> 
                        System.out.println("      " + relation.getType() + " -> " + relation.getDestination())
                    );
            });

            pkg.getInterfaces().forEach(inter -> {
                System.out.println("  Interface: " + inter.getName());
                inter.getMethods().forEach(method -> 
                    System.out.println("    Méthode: " + method.toString())
                );
                System.out.println("    Relations :");
                relations.stream()
                    .filter(relation -> relation.getSource().equals(inter.getName()))
                    .forEach(relation -> 
                        System.out.println("      " + relation.getType() + " -> " + relation.getDestination())
                    );
            });

            pkg.getEnumerations().forEach(enumeration -> {
                System.out.println("  Enumeration: " + enumeration.getName());
                enumeration.getConstants().forEach(constant -> 
                    System.out.println("    Constante: " + constant)
                );
                System.out.println("    Relations :");
                relations.stream()
                    .filter(relation -> relation.getSource().equals(enumeration.getName()))
                    .forEach(relation -> 
                        System.out.println("      " + relation.getType() + " -> " + relation.getDestination())
                    );
            });

            pkg.getAnnotations().forEach(annotation -> {
                System.out.println("  Annotation: " + annotation.getName());
                annotation.getAttributes().forEach(attribute -> 
                    System.out.println("    Attribut: " + attribute)
                );
                System.out.println("    Relations :");
                relations.stream()
                    .filter(relation -> relation.getSource().equals(annotation.getName()))
                    .forEach(relation -> 
                        System.out.println("      " + relation.getType() + " -> " + relation.getDestination())
                    );
            });
        });

        System.out.println("\n=== Fin de l'exploration du projet ===");
    }

    private void generateXMLFile() {
       
        File xmlFile = new File(xmlFilePath);
        if (xmlFile.exists()) {
            System.out.println("Fichier XML déjà existe : " + xmlFilePath);
            return; 
        }

        ProjectExplorer projectExplorer = new ProjectExplorer(projectPath);
        Project project = projectExplorer.getProject();

        XMLFileGenerator xmlGenerator = new XMLFileGenerator(xmlFilePath);
        xmlGenerator.ajouterDeclarationXmlAuDebut();

        for (Package pkg : project.getPackages()) {
            RelationExplorer relationExplorer = new RelationExplorer(pkg.getName());
            List<Relation> relations = relationExplorer.genererRelations(project);

            xmlGenerator.ajouterPackageAuXml(pkg, relations);
        }

        xmlGenerator.fermerXml();
        System.out.println("Fichier XML généré avec succès : " + xmlFilePath);
    }
    
    private void parserXMLFile() {
        XMLParser parser = new XMLParser();
        String cheminXml = "resources/project.xml";
        Project project = parser.parse(cheminXml);
        System.out.println("\n=== Début du parsing ===");

        for (Package pkg : project.getPackages()) {
            System.out.println("Package: " + pkg.getName());

            for (Classe classe : pkg.getClasses()) {
                System.out.println("  Classe: " + classe.getName());
                System.out.print("    Fields: ");
                if (classe.getFields().isEmpty()) {
                    System.out.println("Aucun champ");
                } else {
                    System.out.println(classe.getFields());
                }


                if (classe.getMethods().isEmpty()) {
                    System.out.println("    Aucune méthode");
                } else {
                    System.out.println("    Methods: ");
                    for (MethodInfos method : classe.getMethods()) {
                        System.out.println("      - Name: " + method.getName());
                        System.out.println("        Return Type: " + method.getReturnType());
                        System.out.println("        Visibility: " + method.getMethodVisibility());

                        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
                            System.out.println("        Parameters: " + method.getParameters());
                        } else {
                            System.out.println(" pas de paramétres");
                        }
                    }
                }

                System.out.println("    Relations: ");
                if (classe.getRelationsHeritage().isEmpty() && classe.getRelationImplementation().isEmpty() && classe.getRelationUtilisation().isEmpty()) {
                    System.out.println("      Aucune relation");
                } else {
                    for (Relation relation : classe.getRelationsHeritage()) {
                        System.out.println("      héritage -> " + relation.getDestination());
                    }
                    for (Relation relation : classe.getRelationImplementation()) {
                        System.out.println("      Implementation -> " + relation.getDestination());
                    }
                    for (Relation relation : classe.getRelationUtilisation()) {
                        System.out.println("      Utilisation -> " + relation.getDestination());
                    }
                }
            }

            for (Interface inter : pkg.getInterfaces()) {
                System.out.println("  Interface: " + inter.getName());

                if (inter.getMethods().isEmpty()) {
                    System.out.println("    Aucune méthode");
                } else {
                    System.out.println("    Methodes: ");
                    for (MethodInfos method : inter.getMethods()) {
                        System.out.println("        Name: " + method.getName());
                        System.out.println("        Return Type: " + method.getReturnType());
                        System.out.println("        Visibility: " + method.getMethodVisibility());
                        if (method.getParameters() != null && !method.getParameters().isEmpty()) {
                            System.out.println("        Parameters: " + method.getParameters());
                        } else {
                            System.out.println(" pas de paramétres");
                        }
                    }
                }

                System.out.println("    Relations: ");
                if (inter.getRelationUtilisation().isEmpty()) {
                    System.out.println("      Aucune relation");
                } else {
                    for (Relation relation : inter.getRelationUtilisation()) {
                        System.out.println("      Usage -> " + relation.getDestination());
                    }
                }
            }

            for (Enumeration enumeration : pkg.getEnumerations()) {
                System.out.println("  Enumeration: " + enumeration.getName());
                System.out.println("    Constants: ");
                if (enumeration.getConstants().isEmpty()) {
                    System.out.println("      Aucune constante");
                } else {
                    for (String constant : enumeration.getConstants()) {
                        System.out.println("      - " + constant);
                    }
                }

                System.out.println("    Relations: ");
                if (enumeration.getRelationsUtilisation() == null || enumeration.getRelationsUtilisation().isEmpty()) {
                    System.out.println("      Aucune relation");
                } else {
                    for (Relation relation : enumeration.getRelationsUtilisation()) {
                        System.out.println("      Usage -> " + relation.getDestination());
                    }
                }
            }

            for (Annotation annotation : pkg.getAnnotations()) {
                System.out.println("  Annotation: " + annotation.getName());
                System.out.println("    Attributes: ");
                if (annotation.getAttributes().isEmpty()) {
                    System.out.println("      Aucun attribut");
                } else {
                    for (String attribute : annotation.getAttributes()) {
                        System.out.println("      - " + attribute);
                    }
                }

                System.out.println("    Relations: ");
                if (annotation.getRelationsUtilisation().isEmpty()) {
                    System.out.println("      Aucune relation");
                } else {
                    for (Relation relation : annotation.getRelationsUtilisation()) {
                        System.out.println("      Usage -> " + relation.getDestination());
                    }
                    System.out.println("\n=== Fin du parsing ===");

                }
            }
        }
    }

    public static void main(String[] args) {
        new ConsoleDisplay("src", "resources/project.xml");
    }
}
