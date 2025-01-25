package org.mql.java.xml;

import java.io.*;
import org.mql.java.models.*;
import org.mql.java.models.Package;
import org.mql.java.services.ProjectExplorer;
import org.mql.java.services.RelationExplorer;
import java.util.List;

public class XMLFileGenerator {
    private final String cheminFichier;
    private int indentationLevel = 0;

    public XMLFileGenerator(String cheminFichier) {
        this.cheminFichier = cheminFichier;
        creerFichierXmlVide(); 
    }

    private void creerFichierXmlVide() {
        try {
            File fichier = new File(cheminFichier);
            File dossierParent = fichier.getParentFile();
            if (dossierParent != null && !dossierParent.exists()) {
                dossierParent.mkdirs();
            }

            if (!fichier.exists()) {
                fichier.createNewFile();
                try (FileWriter writer = new FileWriter(fichier)) {
                    writer.write(""); 
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeIndented(FileWriter writer, String content) throws IOException {
        for (int i = 0; i < indentationLevel; i++) {
            writer.write("  "); 
        }
        writer.write(content + "\n");
    }

    private void incrementIndentation() {
        indentationLevel++;
    }

    private void decrementIndentation() {
        indentationLevel--;
    }

    public void ajouterDeclarationXmlAuDebut() {
        try {
            File fichier = new File(cheminFichier);
            if (fichier.exists()) {
                StringBuilder contenuFichier = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(new FileReader(fichier))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        contenuFichier.append(line).append("\n");
                    }
                }

                if (!contenuFichier.toString().startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                    contenuFichier.insert(0, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<Project>\n<Packages>\n");
                }

                try (FileWriter writer = new FileWriter(fichier)) {
                    writer.write(contenuFichier.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ajouterPackageAuXml(Package pkg, List<Relation> relations) {
        try (FileWriter writer = new FileWriter(cheminFichier, true)) {
            writeIndented(writer, "<Package name=\"" + pkg.getName() + "\">");
            incrementIndentation();

            
            if (!pkg.getClasses().isEmpty()) {
                writeIndented(writer, "<Classes>");
                incrementIndentation();
                for (Classe classe : pkg.getClasses()) {
                    writeIndented(writer, "<Classe name=\"" + classe.getName() + "\">");
                    incrementIndentation();

                    for (String field : classe.getFields()) {
                        writeIndented(writer, "<Field>" + field + "</Field>");
                    }

                    for (MethodInfos method : classe.getMethods()) {
                        writeMethod(writer, method);
                    }

                    ajouterRelations(writer, classe, relations);

                    decrementIndentation();
                    writeIndented(writer, "</Classe>");
                }
                decrementIndentation();
                writeIndented(writer, "</Classes>");
            }

            
            if (!pkg.getInterfaces().isEmpty()) {
                writeIndented(writer, "<Interfaces>");
                incrementIndentation();
                for (Interface inter : pkg.getInterfaces()) {
                    writeIndented(writer, "<Interface name=\"" + inter.getName() + "\">");
                    incrementIndentation();

                    for (MethodInfos method : inter.getMethods()) {
                        writeMethod(writer, method);
                    }

                    ajouterRelations(writer, inter, relations);

                    decrementIndentation();
                    writeIndented(writer, "</Interface>");
                }
                decrementIndentation();
                writeIndented(writer, "</Interfaces>");
            }

           
            if (!pkg.getEnumerations().isEmpty()) {
                writeIndented(writer, "<Enumerations>");
                incrementIndentation();
                for (Enumeration enumeration : pkg.getEnumerations()) {
                    writeIndented(writer, "<Enumeration name=\"" + enumeration.getName() + "\">");
                    incrementIndentation();

                    for (String constant : enumeration.getConstants()) {
                        writeIndented(writer, "<Constant>" + constant + "</Constant>");
                    }

                    ajouterRelations(writer, enumeration, relations);

                    decrementIndentation();
                    writeIndented(writer, "</Enumeration>");
                }
                decrementIndentation();
                writeIndented(writer, "</Enumerations>");
            }

            
            if (!pkg.getAnnotations().isEmpty()) {
                writeIndented(writer, "<Annotations>");
                incrementIndentation();
                for (Annotation annotation : pkg.getAnnotations()) {
                    writeIndented(writer, "<Annotation name=\"" + annotation.getName() + "\">");
                    incrementIndentation();

                    for (String attribute : annotation.getAttributes()) {
                        writeIndented(writer, "<Attribute>" + attribute + "</Attribute>");
                    }

                    ajouterRelations(writer, annotation, relations);

                    decrementIndentation();
                    writeIndented(writer, "</Annotation>");
                }
                decrementIndentation();
                writeIndented(writer, "</Annotations>");
            }

            decrementIndentation();
            writeIndented(writer, "</Package>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeMethod(FileWriter writer, MethodInfos method) throws IOException {
        writeIndented(writer, "<Method>");
        incrementIndentation();
        writeIndented(writer, "<name>" + method.getName() + "</name>");
        writeIndented(writer, "<returnType>" + method.getReturnType() + "</returnType>");
        writeIndented(writer, "<methodVisibility>" + method.getMethodVisibility() + "</methodVisibility>");

        if (!method.getParameters().equals("()")) {
            String parameters = method.getParameters().replace("(", "").replace(")", "").trim();
            if (!parameters.isEmpty()) {
                writeIndented(writer, "<parameters>");
                for (String parameter : parameters.split(",")) {
                    writeIndented(writer, "<parameter>" + parameter.trim() + "</parameter>");
                }
                writeIndented(writer, "</parameters>");
            }
        }

        decrementIndentation();
        writeIndented(writer, "</Method>");
    }

    private void ajouterRelations(FileWriter writer, Object element, List<Relation> relations) throws IOException {
        for (Relation relation : relations) {
            if (relation.getSource().equals(getNomElement(element))) {
                writeIndented(writer, "<Relation>");
                incrementIndentation();
                writeIndented(writer, "<type>" + relation.getType() + "</type>");
                writeIndented(writer, "<destination>" + relation.getDestination() + "</destination>");
                decrementIndentation();
                writeIndented(writer, "</Relation>");
            }
        }
    }

    private String getNomElement(Object element) {
        if (element instanceof Classe) {
            return ((Classe) element).getName();
        } else if (element instanceof Interface) {
            return ((Interface) element).getName();
        } else if (element instanceof Annotation) {
            return ((Annotation) element).getName();
        }
        return "introuvable";
    }

    public void fermerXml() {
        try (FileWriter writer = new FileWriter(cheminFichier, true)) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cheminFichier))) {
            	StringBuilder contentBuilder = new StringBuilder();
            	String line;
            	while ((line = reader.readLine()) != null) {
            	    contentBuilder.append(line);
            	}
            	String content = contentBuilder.toString();

                if (!content.contains("</Packages>")) {
                    writeIndented(writer, "</Packages>");
                }
                if (!content.contains("</Project>")) {
                    writeIndented(writer, "</Project>");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  
}
