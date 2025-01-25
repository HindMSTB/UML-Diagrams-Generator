package org.mql.java.services;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

import org.mql.java.models.*;
import org.mql.java.models.Package;

public class RelationExplorer {

    private final String packageRacineProjet;

    public RelationExplorer(String packageRacineProjet) {
        this.packageRacineProjet = packageRacineProjet;
    }

    public List<Relation> genererRelations(Project projet) {
        List<Relation> relations = new Vector<>();

        for (Package pkg : projet.getPackages()) {
            for (Classe cls : pkg.getClasses()) {
                traiterRelationsClasse(pkg, cls, relations);
            }

            for (Annotation annotation : pkg.getAnnotations()) {
                traiterRelationsAnnotation(pkg, annotation, relations);
                
            }

            for (Interface inter : pkg.getInterfaces()) {
                traiterRelationsInterface(pkg, inter, relations);
            }
        }
        return relations;
    }

    private void traiterRelationsClasse(Package pkg, Classe cls, List<Relation> relations) {
        try {
            Class<?> clazz = Class.forName(pkg.getName() + "." + cls.getName());

            traiterHeritage(clazz, cls, relations);
            traiterInterfacesImplémentées(clazz, cls, relations);
            traiterChamps(clazz, cls, relations);
            traiterMethodes(clazz, cls, relations);
            traiterToutesLesAnnotations(clazz, cls, relations);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void traiterToutesLesAnnotations(Class<?> clazz, Classe cls, List<Relation> relations) {
        // Annotations sur la classe
        for (java.lang.annotation.Annotation annotation : clazz.getAnnotations()) {
            ajouterRelationAnnotation(cls.getName(), annotation, relations);
        }

        // Annotations sur les champs
        for (Field field : clazz.getDeclaredFields()) {
            for (java.lang.annotation.Annotation annotation : field.getAnnotations()) {
                ajouterRelationAnnotation(cls.getName(), annotation, relations);
            }
        }

        // Annotations sur les méthodes
        for (Method method : clazz.getDeclaredMethods()) {
            for (java.lang.annotation.Annotation annotation : method.getAnnotations()) {
                ajouterRelationAnnotation(cls.getName(), annotation, relations);
            }
        }
    }

    private void ajouterRelationAnnotation(String elementName, java.lang.annotation.Annotation annotation, List<Relation> relations) {
        Class<?> annotationType = annotation.annotationType();
        if (appartientAuProjet(annotationType)) {
            Relation relation = new Relation(elementName, annotationType.getSimpleName(), "Utilisation");
            relations.add(relation);
        }
    }


    private void traiterHeritage(Class<?> clazz, Classe cls, List<Relation> relations) {
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && appartientAuProjet(superClass)) {
            Relation relation = new Relation(cls.getName(), superClass.getSimpleName(), "Heritage");
            cls.addRelationHeritage(relation);
            relations.add(relation);
        }
    }

    private void traiterInterfacesImplémentées(Class<?> clazz, Classe cls, List<Relation> relations) {
        for (Class<?> iface : clazz.getInterfaces()) {
            if (appartientAuProjet(iface)) {
                Relation relation = new Relation(cls.getName(), iface.getSimpleName(), "Implémente");
                cls.addRelationImplementation(relation);
                relations.add(relation);
            }
        }
    }

    private void traiterChamps(Class<?> clazz, Object element, List<Relation> relations) {
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            if (appartientAuProjet(fieldType)) {
                Relation relation = new Relation(getNomElement(element), fieldType.getSimpleName(), "Agregation");
                relations.add(relation);
            }
        }
    }

    private void traiterMethodes(Class<?> clazz, Object element, List<Relation> relations) {
        for (Method method : clazz.getDeclaredMethods()) {
            for (Class<?> paramType : method.getParameterTypes()) {
                if (appartientAuProjet(paramType)) {
                    Relation relation = new Relation(getNomElement(element), paramType.getSimpleName(), "Utilisation");
                    relations.add(relation);
                }
            }
        }
    }

    private void traiterRelationsAnnotation(Package pkg, Annotation annotation, List<Relation> relations) {
        try {
           
            Class<?> annotationClass = Class.forName(pkg.getName() + "." + annotation.getName());
            if (annotationClass.isAnnotation()) {
              
                for (Field field : annotationClass.getDeclaredFields()) {
                    Class<?> fieldType = field.getType();
                    if (appartientAuProjet(fieldType)) {
                        Relation relation = new Relation(annotation.getName(), fieldType.getSimpleName(), "Utilisation");
                        relations.add(relation);
                    }
                }

               
                for (Method method : annotationClass.getDeclaredMethods()) {
                    Class<?> returnType = method.getReturnType();
                    if (appartientAuProjet(returnType)) {
                        Relation relation = new Relation(annotation.getName(), returnType.getSimpleName(), "Utilisation");
                        relations.add(relation);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void traiterRelationsInterface(Package pkg, Interface inter, List<Relation> relations) {
        try {
            Class<?> interClass = Class.forName(pkg.getName() + "." + inter.getName());
            if (interClass.isInterface()) {
                for (Class<?> extendedInterface : interClass.getInterfaces()) {
                    if (appartientAuProjet(extendedInterface)) {
                        Relation relation = new Relation(inter.getName(), extendedInterface.getSimpleName(), "Heritage");
                        inter.addRelationHeritage(relation);
                        relations.add(relation);
                    }
                }

                traiterMethodes(interClass, inter, relations);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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

    private boolean appartientAuProjet(Class<?> cls) {
        return cls.getName().startsWith(packageRacineProjet);
    }
}    