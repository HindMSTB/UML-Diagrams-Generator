package org.mql.java.examples;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME) 
public @interface testAnnotation1 {
    
    
    String value() default "Default Value";
    
    
    int number() default 0;
}
