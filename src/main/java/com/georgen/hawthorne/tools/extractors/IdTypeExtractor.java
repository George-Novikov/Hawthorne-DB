package com.georgen.hawthorne.tools.extractors;

import com.georgen.hawthorne.api.annotations.Id;
import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.exceptions.TypeException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.messages.TypeMessage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class IdTypeExtractor {
    public static IdType extract(Object object) throws TypeException, HawthorneException {
        Class javaClass = object.getClass();

        IdType idType = getFromFields(javaClass);
        if (idType != null) return idType;

        idType = getFromMethods(javaClass);
        if (idType != null) return idType;

        return IdType.NONE;
    }

    private static IdType getFromFields(Class javaClass) throws TypeException {
        for (Field field : javaClass.getDeclaredFields()){
            if (field.isAnnotationPresent(Id.class) || field.isAnnotationPresent(jakarta.persistence.Id.class)){
                IdType idType = IdType.of(field.getType());
                if (IdType.NONE.equals(idType)) throw new TypeException(TypeMessage.WRONG_ID_TYPE);
                return idType;
            }
        }
        return null;
    }

    private static IdType getFromMethods(Class javaClass) throws TypeException {
        for (Method method : javaClass.getDeclaredMethods()){
            if (method.isAnnotationPresent(Id.class) || method.isAnnotationPresent(jakarta.persistence.Id.class)){
                IdType idType = IdType.of(method.getReturnType());
                if (IdType.NONE.equals(idType)) throw new TypeException(TypeMessage.WRONG_ID_TYPE);
                return idType;
            }
        }
        return null;
    }

}
