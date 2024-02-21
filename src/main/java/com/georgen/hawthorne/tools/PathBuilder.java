package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.storage.StorageArchetype;

import java.io.File;

public class PathBuilder {

    public static String concat(String parentPath, String childPath){
        return String.format("%s%s%s", parentPath, File.separator, childPath);
    }

    public static String getPath(String filePath, EntityType entityType){
        switch (entityType){
            default:
                return getSingletonEntityPath(filePath);
        }
    }

    private static String getSingletonEntityPath(String filePath){
        String entitiesFolderName = StorageSettings.getInstance().getEntitiesPath();
        return concat(entitiesFolderName, String.format("%s.json", filePath));
    }

    private static String getEntityCollectionPath(String filePath){
        String entitiesFolderName = StorageSettings.getInstance().getEntitiesPath();
        return concat(entitiesFolderName, filePath);
    }
}
