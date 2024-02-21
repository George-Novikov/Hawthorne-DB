package com.georgen.hawthorne.tools.extractors;

import com.georgen.hawthorne.api.annotations.BinaryData;
import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ByteContentExtractor {
    public static byte[] extract(Object object) throws HawthorneException, IllegalAccessException, InvocationTargetException {
        Class javaClass = object.getClass();
        validateType(javaClass);

        byte[] data = getFromFields(javaClass, object);
        if (data != null) return data;

        data = getFromMethods(javaClass, object);
        if (data != null) return data;

        throw new HawthorneException(Message.BINARY_DATA_EXTRACTION_ERROR);
    }

    private static byte[] getFromFields(Class javaClass, Object object) throws IllegalAccessException, HawthorneException {
        for (Field field : javaClass.getDeclaredFields()){
            if (field.isAnnotationPresent(BinaryData.class)){
                field.setAccessible(true);
                Object data = field.get(object);
                validateData(data);
                return (byte[]) data;
            }
        }
        return null;
    }

    private static byte[] getFromMethods(Class javaClass, Object object) throws IllegalAccessException, HawthorneException, InvocationTargetException {
        for (Method method : javaClass.getDeclaredMethods()){
            if (method.isAnnotationPresent(BinaryData.class)){
                method.setAccessible(true);
                Object data = method.invoke(object);
                validateData(data);
                return (byte[]) data;
            }
        }
        return null;
    }

    private static void validateType(Class javaClass) throws HawthorneException {
        EntityType entityType = EntityType.of(javaClass);
        if (!entityType.isFile()) throw new HawthorneException(Message.NO_FILE_ANNOTATION);
    }

    private static void validateData(Object data) throws HawthorneException {
        if (data == null) throw new HawthorneException(Message.BINARY_DATA_IS_NULL);
        boolean isByteArray = data instanceof byte[];
        if (isByteArray) throw new HawthorneException(Message.NOT_A_BYTE_ARRAY);
    }
}
