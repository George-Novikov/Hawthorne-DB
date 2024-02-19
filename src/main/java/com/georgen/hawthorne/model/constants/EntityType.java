package com.georgen.hawthorne.model.constants;

import com.georgen.hawthorne.api.annotations.entities.EntityCollection;
import com.georgen.hawthorne.api.annotations.entities.FileCollection;
import com.georgen.hawthorne.api.annotations.entities.SingletonFile;
import com.georgen.hawthorne.api.annotations.entities.SingletonEntity;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.FileMessage;

public enum EntityType {
    SINGLETON_ENTITY,
    ENTITY_COLLECTION,
    SINGLETON_FILE,
    FILE_COLLECTION;

    public static EntityType of(Class javaClass) throws HawthorneException {
        if (javaClass.isAnnotationPresent(SingletonEntity.class)){
            return EntityType.SINGLETON_ENTITY;
        }

        if (javaClass.isAnnotationPresent(EntityCollection.class)){
            return EntityType.ENTITY_COLLECTION;
        }

        if (javaClass.isAnnotationPresent(SingletonFile.class)){
            return EntityType.SINGLETON_FILE;
        }

        if (javaClass.isAnnotationPresent(FileCollection.class)){
            return EntityType.FILE_COLLECTION;
        }

        throw new HawthorneException(FileMessage.NOT_COMPATIBLE);
    }

    public boolean isEntity(){
        return SINGLETON_ENTITY.equals(this) || ENTITY_COLLECTION.equals(this);
    }

    public boolean isFile(){
        return SINGLETON_FILE.equals(this) || FILE_COLLECTION.equals(this);
    }

    public boolean isSingleton(){
        return SINGLETON_ENTITY.equals(this) || SINGLETON_FILE.equals(this);
    }

    public boolean isCollection(){
        return ENTITY_COLLECTION.equals(this) || FILE_COLLECTION.equals(this);
    }
}
