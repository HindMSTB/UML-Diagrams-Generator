package org.mql.java.main;

import org.mql.java.xml.XMLToXMIConverter;

public class XMLToXMIConverterRunner {

    public XMLToXMIConverterRunner() {
        String cheminFichierXml = "resources/project.xml";
        String cheminFichierXmi = "resources/projectXmi.xmi";

        
        XMLToXMIConverter converter = new XMLToXMIConverter(cheminFichierXml, cheminFichierXmi);
        converter.convertir();
    }

    public static void main(String[] args) {
    	new XMLToXMIConverterRunner();
    }
}