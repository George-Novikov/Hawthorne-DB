package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.api.annotations.BinaryData;
import com.georgen.hawthorne.io.FileIOManager;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.StorageArchetype;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class EntityConverter {
    public static <T> T convert(File file, StorageArchetype archetype) throws Exception {
        if (!file.exists()) return null;
        String json = FileIOManager.read(file);
        if (json == null || json.isEmpty()) throw new HawthorneException(Message.FILE_IS_CORRUPTED);

        Class javaClass = Class.forName(archetype.getFullName());
        T object = Serializer.deserialize(json, javaClass);
        if (object == null) throw new HawthorneException(Message.ENTITY_RETRIEVAL_ERROR);

        return object;
    }

    public static <T> T convert(StorageArchetype archetype, Map.Entry<String, File> entry, Map<String, File> binaryDataFiles) throws Exception {
        String id = entry.getKey();

        File entityFile = entry.getValue();
        if (entityFile == null || !entityFile.exists()) return null;

        T object = convert(entityFile, archetype);
        if (object == null) return null;

        File binaryDataFile = binaryDataFiles.get(id);
        if (binaryDataFile == null || !binaryDataFile.exists()) return null;

        byte[] binaryData = FileIOManager.readBytes(binaryDataFile);
        if (binaryData == null) return null;

        return EntityConverter.fillBinaryData(object, binaryData);
    }

    public static <T> T fillBinaryData(T object, byte[] binaryData) throws Exception {
        Class javaClass = object.getClass();

        for (Field field : javaClass.getDeclaredFields()){
            if (field.isAnnotationPresent(BinaryData.class)){
                if (!isByteArray(field)) throw new HawthorneException(Message.NOT_A_BYTE_ARRAY);
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                field.set(object, binaryData);
                field.setAccessible(isAccessible);
                return object;
            }
        }

        for (Method method : javaClass.getDeclaredMethods()){
            if (method.isAnnotationPresent(BinaryData.class)){
                if (!isByteArray(method)) throw new HawthorneException(Message.NOT_A_BYTE_ARRAY);
                boolean isAccessible = method.isAccessible();
                method.setAccessible(true);
                method.invoke(object, binaryData);
                method.setAccessible(isAccessible);
                return object;
            }
        }

        throw new HawthorneException(Message.NO_BINARY_DATA_ANNOTATION);
    }

    private static boolean isByteArray(Field field){
        return field.getType().equals(byte[].class);
    }

    private static boolean isByteArray(Method method) throws HawthorneException {
        Class[] acceptedParameters = method.getParameterTypes();
        if (acceptedParameters.length == 0) throw new HawthorneException(Message.NO_METHOD_PARAMETERS);
        if (acceptedParameters.length > 1) throw new HawthorneException(Message.TOO_MANY_METHOD_PARAMETERS);

        Class methodParameterClass = acceptedParameters[0];
        return methodParameterClass.isAssignableFrom(byte[].class);
    }
}
