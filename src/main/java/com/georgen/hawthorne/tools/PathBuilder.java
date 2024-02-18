package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.storage.StorageArchetype;

import java.io.File;

public class PathBuilder {


    public static String getPath(StorageArchetype archetype){
        switch (archetype.getEntityType()){
            default:
                return getSingletonEntityPath(archetype.getSimpleName());
        }
    }

    public static String getPath(String filePath, EntityType entityType){
        switch (entityType){
            default:
                return getSingletonEntityPath(filePath);
        }
    }

    private static String getSingletonEntityPath(String filePath){
        String entitiesFolderName = StorageSettings.getInstance().getEntitiesPath();
        return String.format("%s%s%s.json", entitiesFolderName, File.separator, filePath);
    }
}
