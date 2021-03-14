package com.bitsolution.addons.simple.cuba.jpa.repositories.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify com.haulmont.addons.cuba.jpa..repository.query for CUBA Query methods
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleJpqlQuery {

    /**
     * JPA com.haulmont.addons.cuba.jpa..repository.query string
     * @return query that should be executed
     */
    String value() default "";

    String countQuery() default "";

}
