package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.api.annotations.Id;
import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;

public class IdGenerator {
    private static final int GENERATION_STEP = 1;

    public static boolean isGenerationRequired(StorageUnit storageUnit) throws HawthorneException {
        if (storageUnit.hasEmptySource()) throw new HawthorneException(Message.SOURCE_IS_NULL);

        EntityType entityType = storageUnit.getArchetype().getEntityType();
        if (!entityType.isCollection()) return false;

        Class javaClass = storageUnit.getSource().getClass();

        for (Field field : javaClass.getDeclaredFields()){
            if (field.isAnnotationPresent(Id.class)){
                return field.getAnnotation(Id.class).isGenerated();
            }
        }

        for (Method method : javaClass.getDeclaredMethods()){
            if (method.isAnnotationPresent(Id.class)){
                return method.getAnnotation(Id.class).isGenerated();
            }
        }

        return false;
    }

    public static void generateForUnit(StorageUnit storageUnit) throws HawthorneException, IllegalAccessException {
        if (storageUnit.hasEmptySource()) throw new HawthorneException(Message.SOURCE_IS_NULL);

        for (Field field : storageUnit.getSource().getClass().getDeclaredFields()){
            if (field.isAnnotationPresent(Id.class)){
                fillIdField(storageUnit, field);
            }
        }
    }

    private static void fillIdField(StorageUnit storageUnit, Field field) throws IllegalAccessException {
        Object source = storageUnit.getSource();
        StorageArchetype archetype = storageUnit.getArchetype();
        IdType idType = archetype.getIdType();
        String lastId = archetype.getLastId();

        field.setAccessible(true);

        switch (idType){
            case UUID:
                String generatedUuid = UUID.randomUUID().toString();
                field.set(source, generatedUuid);
                archetype.setLastId(generatedUuid);
                break;
            case INTEGER:
                int generatedInteger = Integer.valueOf(lastId) + GENERATION_STEP;
                field.set(source, generatedInteger);
                archetype.setLastId(String.valueOf(generatedInteger));
                break;
            case LONG:
                long generatedLong =  Long.valueOf(lastId) + GENERATION_STEP;
                field.set(source, generatedLong);
                archetype.setLastId(String.valueOf(generatedLong));
                break;
        }
    }

}
