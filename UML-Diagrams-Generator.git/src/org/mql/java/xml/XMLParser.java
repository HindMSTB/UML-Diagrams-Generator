package org.mql.java.xml;

import java.lang.reflect.Method;
import java.util.List;
import org.mql.java.models.Annotation;
import org.mql.java.models.Classe;
import org.mql.java.models.Enumeration;
import org.mql.java.models.Interface;
import org.mql.java.models.MethodInfos;
import org.mql.java.models.Package;
import org.mql.java.models.Project;
import org.mql.java.models.Relation;

public class XMLParser {

    public XMLParser() {
       
    }

    public Project parse(String source) {
        Project project = new Project();
        XMLNode root = new XMLNode(source);

        XMLNode packagesNode = root.child("Packages");
        if (packagesNode == null) {
            return project;
        }

        List<XMLNode> packagesList = packagesNode.children();

        for (XMLNode packageNode : packagesList) {
            Package pkg = new Package(packageNode.getAttribute("name"));

            for (XMLNode sectionNode : packageNode.children()) {
                if (sectionNode.getName().equals("Classes")) {
                    for (XMLNode classNode : sectionNode.children()) {
                        traiterClasse(pkg, classNode);
                    }
                } else if (sectionNode.getName().equals("Interfaces")) {
                    for (XMLNode interfaceNode : sectionNode.children()) {
                        traiterInterface(pkg, interfaceNode);
                    }
                } else if (sectionNode.getName().equals("Annotations")) {
                    for (XMLNode annotationNode : sectionNode.children()) {
                        traiterAnnotation(pkg, annotationNode);
                    }
                } else if (sectionNode.getName().equals("Enumerations")) {
                    for (XMLNode enumNode : sectionNode.children()) {
                        traiterEnum(pkg, enumNode);
                    }
                } 
            }

            project.addPackage(pkg);
        }

        return project;
    }

    private void traiterClasse(Package pkg, XMLNode elementNode) {
        Classe classe = new Classe(elementNode.getAttribute("name"));
        if (classe.getName() == null || classe.getName().isEmpty()) {
            System.out.println("Erreur : une classe sans nom a été trouvée.");
            return;
        }

        for (XMLNode childNode : elementNode.children()) {
            if (childNode.getName().equals("Field")) {
                classe.addField(childNode.getValue());
            } else if (childNode.getName().equals("Method")) {
                String methodName = childNode.child("name").getValue();
                String returnType = childNode.child("returnType").getValue();
                String visibility = childNode.child("methodVisibility").getValue();

                String parameters = "";
                XMLNode parametersNode = childNode.child("parameters");
                if (parametersNode != null) {
                    List<XMLNode> parameterNodes = parametersNode.children();
                    StringBuilder parametersBuilder = new StringBuilder();
                    for (XMLNode parameterNode : parameterNodes) {
                        if (parameterNode.getName().equals("parameter")) {
                            if (parametersBuilder.length() > 0) {
                                parametersBuilder.append(", ");
                            }
                            parametersBuilder.append(parameterNode.getValue());
                        }
                    }
                    parameters = parametersBuilder.toString();
                }

                MethodInfos methodInfos = new MethodInfos(methodName, returnType, visibility);
                methodInfos.setParameters(parameters);
                classe.addMethod(methodInfos);
            } else if (childNode.getName().equals("Relation")) {
                ajouterRelation(classe, childNode);
            } else {
                System.out.println("Type inconnu pour le nœud enfant : " + childNode.getName());
            }
        }

        pkg.addClass(classe);
    }

    private void traiterInterface(Package pkg, XMLNode elementNode) {
        Interface inter = new Interface(elementNode.getAttribute("name"));
        
        if (inter.getName() == null || inter.getName().isEmpty()) {
            return;
        }

        for (XMLNode childNode : elementNode.children()) {
            if (childNode.getName().equals("Method")) {
                String methodName = childNode.child("name").getValue();
                String returnType = childNode.child("returnType").getValue();
                String methodVisibility = childNode.child("methodVisibility").getValue();

                String parameters = "";
                XMLNode parametersNode = childNode.child("parameters");
                if (parametersNode != null) {
                    List<XMLNode> parameterNodes = parametersNode.children();
                    StringBuilder parametersBuilder = new StringBuilder();
                    for (XMLNode parameterNode : parameterNodes) {
                        if (parameterNode.getName().equals("parameter")) {
                            if (parametersBuilder.length() > 0) {
                                parametersBuilder.append(", ");
                            }
                            parametersBuilder.append(parameterNode.getValue());
                        }
                    }
                    parameters = parametersBuilder.toString();
                }

                MethodInfos methodInfos = new MethodInfos(methodName, returnType, methodVisibility);
                methodInfos.setParameters(parameters);

                inter.addMethod(methodInfos);
            } else if (childNode.getName().equals("Relation")) {
                ajouterRelation(inter, childNode);
            } else {
                System.out.println("Type inconnu pour le nœud enfant : " + childNode.getName());
            }
        }

        pkg.addInterface(inter);
    }

    private void traiterAnnotation(Package pkg, XMLNode elementNode) {
        Annotation annotation = new Annotation(elementNode.getAttribute("name"));
        if (annotation.getName() == null || annotation.getName().isEmpty()) {
            return;
        }

        for (XMLNode childNode : elementNode.children()) {
            if (childNode.getName().equals("Attribute")) {
                annotation.addAttribute(childNode.getValue());
            } else {
                System.err.println("Type inconnu pour le nœud enfant : " + childNode.getName());
            }
        }

        pkg.addAnnotation(annotation);
    }

    private void traiterEnum(Package pkg, XMLNode elementNode) {
        Enumeration enumeration = new Enumeration(elementNode.getAttribute("name"));
        if (enumeration.getName() == null || enumeration.getName().isEmpty()) {
            return;
        }

        for (XMLNode childNode : elementNode.children()) {
            if (childNode.getName().equals("Constant")) {
                enumeration.addConstant(childNode.getValue());
            } else if (childNode.getName().equals("Relation")) {
                ajouterRelation(enumeration, childNode);
            } else {
                System.out.println("Type inconnu pour le nœud enfant : " + childNode.getName());
            }
        }

        pkg.addEnumeration(enumeration);
    }

    private void ajouterRelation(Object entity, XMLNode relationNode) {
        String relType = relationNode.child("type").getValue();
        String destination = relationNode.child("destination").getValue();
        Relation relation = new Relation(getNomElement(entity), destination, relType);

        if (entity instanceof Classe) {
            if ("inheritance".equalsIgnoreCase(relType)) {
                ((Classe) entity).addRelationHeritage(relation);
            } else if ("implementation".equalsIgnoreCase(relType)) {
                ((Classe) entity).addRelationImplementation(relation);
            } else {
                ((Classe) entity).addRelationUtilisation(relation);
            }
        } else if (entity instanceof Interface) {
            ((Interface) entity).addRelationUtilisation(relation);
        } else if (entity instanceof Annotation) {
            ((Annotation) entity).addRelationUtilisation(relation);
        }
    }

    private String getNomElement(Object entity) {
        if (entity instanceof Classe) {
            return ((Classe) entity).getName();
        } else if (entity instanceof Interface) {
            return ((Interface) entity).getName();
        } else if (entity instanceof Annotation) {
            return ((Annotation) entity).getName();
        } else if (entity instanceof Enumeration) {
            return ((Enumeration) entity).getName();
        }
        return null;
    }

    
        
    }

