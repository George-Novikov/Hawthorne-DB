package com.georgen.hawthorne.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityCollection {
    String name() default "";
    String path() default "";
    boolean hasRelativePath() default false;
}