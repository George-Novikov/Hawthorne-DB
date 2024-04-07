package com.georgen.hawthorne.model.constants;

import com.georgen.hawthorne.api.annotations.EntityCollection;
import com.georgen.hawthorne.api.annotations.SingletonEntity;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.tools.extractors.BinaryDataExtractor;

public enum EntityType {
    SINGLETON_ENTITY,
    ENTITY_COLLECTION,
    SINGLETON_FILE,
    FILE_COLLECTION;

    public static EntityType of(Object object) throws HawthorneException {
        Class javaClass = object.getClass();

        if (javaClass.isAnnotationPresent(SingletonEntity.class)){
            return hasBinaryData(object) ? SINGLETON_FILE : SINGLETON_ENTITY;
        }

        boolean hasPersistenceEntityAnnotation = javaClass.isAnnotationPresent(jakarta.persistence.Entity.class)
                || javaClass.isAnnotationPresent(javax.persistence.Entity.class);

        if (javaClass.isAnnotationPresent(EntityCollection.class) || hasPersistenceEntityAnnotation){
            return hasBinaryData(object) ? FILE_COLLECTION : ENTITY_COLLECTION;
        }

        throw new HawthorneException(Message.NOT_ANNOTATED);
    }

    public static boolean isAnnotated(Class javaClass){
        return javaClass.isAnnotationPresent(SingletonEntity.class)
                || javaClass.isAnnotationPresent(EntityCollection.class)
                || javaClass.isAnnotationPresent(jakarta.persistence.Entity.class)
                || javaClass.isAnnotationPresent(javax.persistence.Entity.class);
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
    private static boolean hasBinaryData(Object object){
        return BinaryDataExtractor.isAnnotatedAsBinaryData(object);
    }
}
