package com.georgen.hawthorne.tools.id;

import com.georgen.hawthorne.api.annotations.Id;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class IdAnnotationDetector {
    public static boolean hasIdAnnotation(Field field){
        return field.isAnnotationPresent(Id.class)
                || field.isAnnotationPresent(jakarta.persistence.Id.class)
                || field.isAnnotationPresent(javax.persistence.Id.class);
    }

    public static boolean hasIdAnnotation(Method method){
        return method.isAnnotationPresent(Id.class)
                || method.isAnnotationPresent(jakarta.persistence.Id.class)
                || method.isAnnotationPresent(javax.persistence.Id.class);
    }
}
