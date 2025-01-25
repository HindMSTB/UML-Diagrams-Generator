package org.mql.java.xml;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class XMLToXMIConverter {

    private final String cheminFichierXml;
    private final String cheminFichierXmi;

    public XMLToXMIConverter(String cheminFichierXml, String cheminFichierXmi) {
        this.cheminFichierXml = cheminFichierXml;
        this.cheminFichierXmi = cheminFichierXmi;
    }

    public void convertir() {
        try {
            File resourcesFolder = new File("resources");
            if (!resourcesFolder.exists()) {
                resourcesFolder.mkdirs();
            }

            File xmiFile = new File(cheminFichierXmi);
            if (xmiFile.exists()) {
                System.out.println("Le fichier XMI existe déjà. Aucun fichier n'a été créé.");
                return;
            }

            File xmlFile = new File(cheminFichierXml);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            FileWriter writer = new FileWriter(cheminFichierXmi);
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<xmi:XMI xmlns:xmi=\"http://www.omg.org/spec/XMI/2.1\" xmlns:uml=\"http://www.omg.org/spec/UML/2.1\">\n");

            NodeList packages = document.getElementsByTagName("Package");
            for (int i = 0; i < packages.getLength(); i++) {
                Element packageElement = (Element) packages.item(i);
                String packageName = packageElement.getAttribute("name");

                writer.write("    <xmi:Package xmi:id=\"Package" + (i + 1) + "\" name=\"" + packageName + "\">\n");

                NodeList classes = packageElement.getElementsByTagName("Classe");
                for (int j = 0; j < classes.getLength(); j++) {
                    Element classElement = (Element) classes.item(j);
                    String className = classElement.getAttribute("name");

                    writer.write("        <uml:Class xmi:id=\"Classe" + (j + 1) + "\" name=\"" + className + "\">\n");

                    NodeList fields = classElement.getElementsByTagName("Field");
                    for (int k = 0; k < fields.getLength(); k++) {
                        Element fieldElement = (Element) fields.item(k);
                        String fieldContent = fieldElement.getTextContent();
                        String[] fieldParts = fieldContent.split(" : ");
                        String fieldName = fieldParts[0].trim();
                        String fieldType = fieldParts[1].split(" ")[0].trim();
                        String visibility = fieldContent.contains("private") ? "private" : "public";

                        writer.write("            <uml:Attribute name=\"" + fieldName + "\" visibility=\"" + visibility + "\" type=\"" + fieldType + "\"/>\n");
                    }

                    NodeList methods = classElement.getElementsByTagName("Method");
                    for (int l = 0; l < methods.getLength(); l++) {
                        Element methodElement = (Element) methods.item(l);
                        String methodName = methodElement.getElementsByTagName("name").item(0).getTextContent();
                        String returnType = methodElement.getElementsByTagName("returnType").item(0).getTextContent();
                        String methodVisibility = methodElement.getElementsByTagName("methodVisibility").item(0).getTextContent();

                        writer.write("            <uml:Operation name=\"" + methodName + "\" returnType=\"" + returnType + "\" visibility=\"" + methodVisibility + "\"/>\n");
                    }

                    ajouterRelations(writer, classElement);

                    writer.write("        </uml:Class>\n");
                }

                writer.write("    </xmi:Package>\n");
            }

            writer.write("</xmi:XMI>\n");

            writer.close();
            System.out.println("Conversion terminée. Le fichier XMI a été généré avec succès.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ajouterRelations(FileWriter writer, Element classElement) throws IOException {
        NodeList relations = classElement.getElementsByTagName("Relation");
        for (int m = 0; m < relations.getLength(); m++) {
            Element relationElement = (Element) relations.item(m);
            String relationType = relationElement.getElementsByTagName("type").item(0).getTextContent();
            String destination = relationElement.getElementsByTagName("destination").item(0).getTextContent();

            if (relationType.equals("Implémente")) {
                writer.write("            <uml:Realization xmi:id=\"" + "Realization" + "\" client=\"" + classElement.getAttribute("name") + "\" supplier=\"" + destination + "\"/>\n");
            } else if (relationType.equals("Agregation")) {
                writer.write("            <uml:Association xmi:id=\"" + "Association" + "\" memberEnd=\"" + classElement.getAttribute("name") + ", " + destination + "\"/>\n");
            }
        }
    }

    
}
