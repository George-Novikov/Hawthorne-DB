package com.georgen.hawthorne.tools.id.extractors;

import com.georgen.hawthorne.tools.id.IdAnnotationDetector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class IdValueExtractor {
    public static Object extract(Object object) throws IllegalAccessException, InvocationTargetException {
        Class javaClass = object.getClass();

        Object value = extractFromField(javaClass, object);
        if (value != null) return value;

        return extractFromMethod(javaClass, object);
    }

    private static Object extractFromField(Class javaClass, Object object) throws IllegalAccessException {
        for (Field field : javaClass.getDeclaredFields()){
            if (IdAnnotationDetector.hasIdAnnotation(field)){
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                Object value = field.get(object);
                field.setAccessible(isAccessible);
                return value;
            }
        }
        return null;
    }

    private static Object extractFromMethod(Class javaClass, Object object) throws IllegalAccessException, InvocationTargetException {
        for (Method method : javaClass.getDeclaredMethods()){
            if (IdAnnotationDetector.hasIdAnnotation(method)){
                boolean isAccessible = method.isAccessible();
                method.setAccessible(true);
                Object value = method.invoke(object);
                method.setAccessible(isAccessible);
                return value;
            }
        }
        return null;
    }
}
