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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class IdGenerator {

    public static boolean isGenerationRequired(StorageUnit storageUnit) throws HawthorneException, IllegalAccessException, InvocationTargetException {
        if (storageUnit.hasEmptySource()) throw new HawthorneException(Message.SOURCE_IS_NULL);

        EntityType entityType = storageUnit.getArchetype().getEntityType();
        if (!entityType.isCollection()) return false;

        Object source = storageUnit.getSource();
        Class javaClass = source.getClass();

        for (Field field : javaClass.getDeclaredFields()){
            if (field.isAnnotationPresent(Id.class)){
                field.setAccessible(true);
                return isValueNull(field, source) ? field.getAnnotation(Id.class).isGenerated() : false;
            }
        }

        for (Method method : javaClass.getDeclaredMethods()){
            if (method.isAnnotationPresent(Id.class)){
                method.setAccessible(true);
                return isValueNull(method, source) ? method.getAnnotation(Id.class).isGenerated() : false;
            }
        }

        return false;
    }

    private static boolean isValueNull(Field field, Object source) throws IllegalAccessException {
        Object value = field.get(source);
        String stringValue = value != null ? String.valueOf(value) : "0";
        return value == null || stringValue.equals("0") || stringValue.equals("0L");
    }

    private static boolean isValueNull(Method method, Object source) throws InvocationTargetException, IllegalAccessException {
        Object value = method.invoke(source);
        String stringValue = value != null ? String.valueOf(value) : "0";
        return value == null || stringValue.equals("0") || stringValue.equals("0L");
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

        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);

        switch (idType){
            case UUID:
                String generatedUuid = ((UuidCounter) idCounter).getNext();
                field.set(source, generatedUuid);
                field.setAccessible(isAccessible);
                return generatedUuid;
            case INTEGER:
                int generatedInteger = ((IntegerCounter) idCounter).getNext();
                field.set(source, generatedInteger);
                field.setAccessible(isAccessible);
                return generatedInteger;
            case LONG:
                long generatedLong = ((LongCounter) idCounter).getNext();
                field.set(source, generatedLong);
                field.setAccessible(isAccessible);
                return generatedLong;
            default:
                return null;
        }
    }

}
