package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.api.annotations.Id;
import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.exceptions.TypeException;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.tools.extractors.IdTypeExtractor;

import java.lang.reflect.Field;
import java.util.UUID;

public class IdGenerator {
    public static <T> T fill(StorageUnit storageUnit, T object) throws HawthorneException, TypeException {
        IdType idType = storageUnit.getArchetype().getIdType();

        for (Field field : object.getClass().getDeclaredFields()){
            if (field.isAnnotationPresent(Id.class)){

            }
        }

        return object;
    }

    private static <T> void fillIdField(T object, Field field, IdType idType) throws IllegalAccessException {
        field.setAccessible(true);

        switch (idType){
            case UUID:
                field.set(object, UUID.randomUUID().toString());
        }
    }

}
