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
            return SINGLETON_ENTITY;
        }

        if (javaClass.isAnnotationPresent(EntityCollection.class)){
            return ENTITY_COLLECTION;
        }

//        if (javaClass.isAnnotationPresent(SingletonEntity.class)){
//            return hasBinaryData(object) ? SINGLETON_FILE : SINGLETON_ENTITY;
//        }
//
//        if (javaClass.isAnnotationPresent(EntityCollection.class)){
//            return hasBinaryData(object) ? FILE_COLLECTION : ENTITY_COLLECTION;
//        }

        throw new HawthorneException(Message.NOT_COMPATIBLE);
    }

    public static boolean isTyped(Class javaClass){
        return javaClass.isAnnotationPresent(SingletonEntity.class) || javaClass.isAnnotationPresent(EntityCollection.class);
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
