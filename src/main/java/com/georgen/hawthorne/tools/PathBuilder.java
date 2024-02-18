package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.config.Settings;
import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.storage.StorageArchetype;

import java.io.File;

public class PathBuilder {


    public static String getPath(StorageArchetype unit){
        switch (unit.getEntityType()){
            default:
                return getSingletonEntityPath(unit.getSimpleName());
        }
    }

    public static String getPath(String filePath, EntityType entityType){
        switch (entityType){
            default:
                return getSingletonEntityPath(filePath);
        }
    }

    private static String getSingletonEntityPath(String filePath){
        String entitiesFolderName = Settings.getInstance().getEntitiesPath();
        return String.format("%s%s%s.json", entitiesFolderName, File.separator, filePath);
    }
}
