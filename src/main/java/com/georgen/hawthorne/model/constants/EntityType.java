package com.georgen.hawthorne.model.constants;

import com.georgen.hawthorne.api.annotations.entities.EntityCollection;
import com.georgen.hawthorne.api.annotations.entities.FileEntity;
import com.georgen.hawthorne.api.annotations.entities.MonoEntity;
import com.georgen.hawthorne.model.exceptions.FileException;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.FileMessage;

public enum EntityType {
    MONO_ENTITY,
    ENTITY_COLLECTION,
    FILE;

    public static EntityType of(Class javaClass) throws FileException {
        if (javaClass.isAnnotationPresent(MonoEntity.class)){
            return EntityType.MONO_ENTITY;
        }

        if (javaClass.isAnnotationPresent(EntityCollection.class)){
            return EntityType.ENTITY_COLLECTION;
        }

        if (javaClass.isAnnotationPresent(FileEntity.class)){
            return EntityType.FILE;
        }

        throw new FileException(FileMessage.FILE_IS_NOT_AN_ENTITY);
    }
}
