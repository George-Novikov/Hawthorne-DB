package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.api.annotations.EntityCollection;
import com.georgen.hawthorne.api.annotations.SingletonEntity;
import com.georgen.hawthorne.model.constants.SystemProperty;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.model.constants.EntityType;

import java.io.File;

/**
 * @SingletonEntity path: root + entities + customPath + classSimpleName + partitionNumber + classSimpleName.json
 * @EntityCollention path: root + entities + customPath + classSimpleName + partitionNumber + id.json
 * @BinaryData path: root + entities + customPath + classSimpleName + partitionNumber + binary-data + id.bin or classSimpleName.bin
 *
 */
public class PathBuilder {
    private static final String ENTITY_FILE_EXTENSION = "json";
    private static final String BINARY_DATA_EXTENSION = "bin";

    public static String concat(String parentPath, String childPath){
        return String.format("%s%s%s", parentPath, File.separator, childPath);
    }

    public static String buildBasePath(Object object, StorageArchetype archetype){
        return buildBasePath(object, archetype, 0);
    }

    public static String buildBasePath(Object object, StorageArchetype archetype, int id){
        EntityType entityType = archetype.getEntityType();
        return entityType.isCollection() ? getCollectionPath(object, archetype, id) : getSingletonPath(object);
    }

    private static String getSingletonPath(Object object){
        String entitiesBasePath = StorageSettings.getInstance().getEntitiesPath();
        String customSourcePath = extractPathFromAnnotation(object);
        return concat(entitiesBasePath, customSourcePath);
    }

    private static String getCollectionPath(Object object, StorageArchetype archetype, int id){
        String entitiesBasePath = StorageSettings.getInstance().getEntitiesPath();
        String customSourcePath = extractPathFromAnnotation(object);
        String archetypeSimpleName = archetype.getSimpleName();
        //TODO: change the path building process to locate the partitions number last
        String partitionPath = id < 1 ? String.valueOf(archetype.getPartitionsCounter()) : String.valueOf(PartitionManager.locatePartition(archetype, id));
        return concat(
                concat(entitiesBasePath, customSourcePath),
                concat(archetypeSimpleName, partitionPath)
        );
    }

    public static String toEntityPath(StorageArchetype archetype){
        EntityType entityType = archetype.getEntityType();
        String basePath = archetype.getPath();
        String documentPath = entityType.isCollection() ? concat(basePath, archetype.getLastId()) : concat(basePath, archetype.getSimpleName());
        return String.format("%s.%s", documentPath, ENTITY_FILE_EXTENSION);
    }

    public static String toBinaryDataPath(StorageArchetype archetype){
        EntityType entityType = archetype.getEntityType();
        String binaryDataPath = StorageSettings.getInstance().getBinaryDataPath();
        String basePath = concat(archetype.getPath(), binaryDataPath);
        String filePath = entityType.isCollection() ? concat(basePath, archetype.getLastId()) : concat(basePath, archetype.getSimpleName());
        return String.format("%s.%s", filePath, BINARY_DATA_EXTENSION);
    }

    public static String toIdCounterPath(String path){
        return concat(path, SystemProperty.ID_COUNTER_NAME.getValue());
    }

    public static String extractPathFromAnnotation(Object object) {
        Class javaClass = object.getClass();

        if (javaClass.isAnnotationPresent(SingletonEntity.class)){
            SingletonEntity singletonEntityAnnotation = (SingletonEntity) javaClass.getAnnotation(SingletonEntity.class);
            return formatSeparators(singletonEntityAnnotation.path());
        }

        if (javaClass.isAnnotationPresent(EntityCollection.class)){
            EntityCollection entityCollectionAnnotation = (EntityCollection) javaClass.getAnnotation(EntityCollection.class);
            return formatSeparators(entityCollectionAnnotation.path());
        }

        return null;
    }

    public static String formatSeparators(String string){
        try {
            if (string.startsWith("/") || string.startsWith("\\")) string = string.substring(1);
            return string.replace("/", File.separator).replace("\\", File.separator);
        } catch (Exception e){
            return string;
        }
    }
}
