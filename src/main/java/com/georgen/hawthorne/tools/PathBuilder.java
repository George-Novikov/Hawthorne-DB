package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.api.annotations.EntityCollection;
import com.georgen.hawthorne.api.annotations.SingletonEntity;
import com.georgen.hawthorne.model.constants.SystemProperty;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.model.constants.EntityType;

import java.io.File;

/**
 * @SingletonEntity path: hawthorneRoot/entities/customPath/classSimpleName/classSimpleName.json
 * @BinaryData (singleton) path: hawthorneRoot/entities/customPath/classSimpleName/classSimpleName.bin
 *
 * @EntityCollention path: hawthorneRoot/entities/customPath/classSimpleName/partitionNumber/id.json
 * @BinaryData (collection) path: hawthorneRoot/entities/customPath/classSimpleName/partitionNumber/id.bin
 */
public class PathBuilder {

    public static String concatenate(String parentPath, String childPath){
        return String.format("%s%s%s", parentPath, File.separator, childPath);
    }

    public static String buildBasePath(Object source, StorageArchetype archetype){
        String entitiesBasePath = StorageSettings.getInstance().getEntitiesPath();
        String customSourcePath = extractPathFromAnnotation(source);
        return concatenate(
                concatenate(entitiesBasePath, customSourcePath),
                archetype.getSimpleName()
        );
    }

    public static String getEntityPath(StorageArchetype archetype) {
        return getEntityPath(archetype, null);
    }

    public static String getEntityPath(StorageArchetype archetype, Object id) {
        return getFinalFilePath(archetype, id, SystemProperty.ENTITY_FILE_EXTENSION.getValue());
    }

    public static String getBinaryDataPath(StorageArchetype archetype, Object id) {
        return getFinalFilePath(archetype, id, SystemProperty.BINARY_DATA_EXTENSION.getValue());
    }

    public static String getFinalFilePath(StorageArchetype archetype, Object id, String fileExtension) {
        EntityType entityType = archetype.getEntityType();

        String basePath = archetype.getPath();
        String entityName = entityType.isSingleton() ? archetype.getSimpleName() : String.valueOf(id != null ? id : 0);

        return concatenate(
                basePath,
                String.format("%s.%s", entityName, fileExtension)
        );
    }

    public static String getIdCounterPath(StorageArchetype archetype){
        return concatenate(
                archetype.getPath(),
                SystemProperty.ID_COUNTER_NAME.getValue());
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
            /** Currently only custom paths relative to the root are supported */
            if (string.startsWith("/") || string.startsWith("\\")) string = string.substring(1);
            return string.replace("/", File.separator).replace("\\", File.separator);
        } catch (Exception e){
            return string;
        }
    }
}
