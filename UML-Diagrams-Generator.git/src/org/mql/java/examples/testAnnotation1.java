package org.mql.java.examples;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Déclaration de l'annotation
@Target(ElementType.METHOD) // Cette annotation peut être utilisée sur des méthodes
@Retention(RetentionPolicy.RUNTIME) // L'annotation sera disponible à l'exécution
public @interface testAnnotation1 {
    
    // Exemple d'attribut de l'annotation
    String value() default "Default Value";
    
    // Exemple d'attribut avec un type complexe
    int number() default 0;
}
