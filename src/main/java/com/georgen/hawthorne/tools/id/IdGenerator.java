package com.georgen.hawthorne.tools.id;

import com.georgen.hawthorne.api.annotations.Id;
import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.tools.id.counters.IdCounter;
import com.georgen.hawthorne.tools.id.counters.IntegerCounter;
import com.georgen.hawthorne.tools.id.counters.LongCounter;
import com.georgen.hawthorne.tools.id.counters.UuidCounter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class IdGenerator {

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

    public static Object generateForUnit(StorageUnit storageUnit) throws Exception {
        if (storageUnit.hasEmptySource()) throw new HawthorneException(Message.SOURCE_IS_NULL);
        Object generatedId = null;

        for (Field field : storageUnit.getSource().getClass().getDeclaredFields()){
            if (!field.isAnnotationPresent(Id.class)) continue;
            generatedId = fillIdField(storageUnit, field);
        }

       return generatedId;
    }

    private static Object fillIdField(StorageUnit storageUnit, Field field) throws Exception {
        Object source = storageUnit.getSource();
        StorageArchetype archetype = storageUnit.getArchetype();
        IdType idType = archetype.getIdType();
        IdCounter idCounter = IdCounterFactory.getInstance().getCounter(archetype);
        if (idCounter == null) return null;

        field.setAccessible(true);

        switch (idType){
            case UUID:
                String generatedUuid = ((UuidCounter) idCounter).getNext();
                field.set(source, generatedUuid);
                archetype.setLastId(generatedUuid);
                return generatedUuid;
            case INTEGER:
                int generatedInteger = ((IntegerCounter) idCounter).getNext();
                field.set(source, generatedInteger);
                archetype.setLastId(String.valueOf(generatedInteger));
                return generatedInteger;
            case LONG:
                long generatedLong = ((LongCounter) idCounter).getNext();
                field.set(source, generatedLong);
                archetype.setLastId(String.valueOf(generatedLong));
                return generatedLong;
            default:
                return null;
        }
    }

}
