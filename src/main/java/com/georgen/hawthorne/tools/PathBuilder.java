package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.api.annotations.EntityCollection;
import com.georgen.hawthorne.api.annotations.SingletonEntity;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.model.constants.EntityType;

import java.io.File;

/**
 * @SingletonEntity path: root + entities + customPath + classSimpleName + partitionNumber + classSimpleName.json
 * @EntityCollention path: root + entities + customPath + classSimpleName + partitionNumber + id.json
 * @BinaryData path: root + entities + customPath + classSimpleName + partitionNumber + binary-data + id/classSimpleName.json
 *
 */
public class PathBuilder {
    //
    // JSON file path: root + entities + fileSimpleName + partition +

    public static String concat(String parentPath, String childPath){
        return String.format("%s%s%s", parentPath, File.separator, childPath);
    }

    public static String getPath(StorageArchetype archetype){
        return null;
    }

    public static String getPath(String filePath, EntityType entityType){
        switch (entityType){
            default:
                return getSingletonPath(filePath);
        }
    }

    private static String getSingletonPath(String filePath){
        String entitiesFolderName = StorageSettings.getInstance().getEntitiesPath();
        return concat(entitiesFolderName, String.format("%s.json", filePath));
    }

    private static String getCollectionPath(String filePath){
        String entitiesFolderName = StorageSettings.getInstance().getEntitiesPath();
        return concat(entitiesFolderName,filePath);
    }

    private static String getEntityCollectionPath(String filePath){
        String entitiesFolderName = StorageSettings.getInstance().getEntitiesPath();
        return concat(entitiesFolderName, filePath);
    }

    public static String extractPathFromAnnotation(Object object) {
        Class javaClass = object.getClass();

        if (javaClass.isAnnotationPresent(SingletonEntity.class)){
            SingletonEntity singletonEntityAnnotation = (SingletonEntity) javaClass.getAnnotation(SingletonEntity.class);
            return singletonEntityAnnotation.path();
        }

        if (javaClass.isAnnotationPresent(EntityCollection.class)){
            EntityCollection entityCollectionAnnotation = (EntityCollection) javaClass.getAnnotation(EntityCollection.class);
            return entityCollectionAnnotation.path();
        }

        return null;
    }
}
