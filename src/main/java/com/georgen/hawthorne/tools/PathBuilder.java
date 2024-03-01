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

    public static String concatenate(String parentPath, String childPath){
        return String.format("%s%s%s", parentPath, File.separator, childPath);
    }

    public static String getIdCounterPath(StorageArchetype archetype){
        return concatenate(
                archetype.getPath(),
                SystemProperty.ID_COUNTER_NAME.getValue()
        );
    }

    public static String getUuidIndexPath(StorageArchetype archetype){
        return concatenate(
                archetype.getPath(),
                String.format(
                        "%s-%s",
                        SystemProperty.UUID_INDEX_NAME.getValue(),
                        archetype.getPartitionsCounter()
                )
        );
    }

    public static String buildBasePath(Object object, StorageArchetype archetype){
        EntityType entityType = archetype.getEntityType();
        return entityType.isCollection() ? getCollectionPath(object, archetype) : getSingletonPath(object);
    }

    private static String getSingletonPath(Object object){
        String entitiesBasePath = StorageSettings.getInstance().getEntitiesPath();
        String customSourcePath = extractPathFromAnnotation(object);
        return concatenate(entitiesBasePath, customSourcePath);
    }

    private static String getCollectionPath(Object object, StorageArchetype archetype){
        String entitiesBasePath = StorageSettings.getInstance().getEntitiesPath();
        String customSourcePath = extractPathFromAnnotation(object);
        return concatenate(
                concatenate(entitiesBasePath, customSourcePath),
                archetype.getSimpleName()
        );
    }

    public static String buildEntityPath(StorageArchetype archetype) throws Exception {
        return buildEntityPath(archetype, false);
    }

    public static String buildEntityPath(StorageArchetype archetype, boolean isNewFile) throws Exception {
        return buildEntityPath(archetype, isNewFile, null);
    }

    public static String buildEntityPath(StorageArchetype archetype, boolean isNewFile, Object id) throws Exception {
        EntityType entityType = archetype.getEntityType();
        return entityType.isSingleton() ? getSingletonEntityPath(archetype) : getEntityCollectionPath(archetype, isNewFile, id);
    }

    public static String getSingletonEntityPath(StorageArchetype archetype){
        String basePath = archetype.getPath();
        String fileName = String.format("%s.%s", archetype.getSimpleName(), ENTITY_FILE_EXTENSION);
        return concatenate(basePath, fileName);
    }

    public static String getEntityCollectionPath(StorageArchetype archetype, boolean isNewFile, Object id) throws Exception {
        String basePath = archetype.getPath();
        int partitionNumber = PartitionManager.locatePartition(archetype, isNewFile, id);
        return concatenate(
                concatenate(basePath, String.valueOf(partitionNumber)),
                String.format("%s.%s", id, ENTITY_FILE_EXTENSION)
        );
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

        return "";
    }

    public static String formatSeparators(String string){
        try {
            /** Only relative paths are supported at the moment */
            if (string.startsWith("/") || string.startsWith("\\")) string = string.substring(1);
            return string.replace("/", File.separator).replace("\\", File.separator);
        } catch (Exception e){
            return string;
        }
    }
}
