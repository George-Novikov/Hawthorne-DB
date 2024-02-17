package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.config.Settings;
import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.storage.StorageUnit;

import java.io.File;

public class PathBuilder {


    public static String getPath(StorageUnit unit){
        switch (unit.getEntityType()){
            default:
                return getMonoEntityPath(unit.getSimpleName());
        }
    }

    public static String getPath(String filePath, EntityType entityType){
        switch (entityType){
            default:
                return getMonoEntityPath(filePath);
        }
    }

    private static String getMonoEntityPath(String filePath){
        String entitiesFolderName = Settings.getInstance().getEntitiesPath();
        return String.format("%s%s%s", entitiesFolderName, File.separator, filePath);
    }
}
