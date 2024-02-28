package com.georgen.hawthorne.repositories;

import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;

public class RepositoryFactory {

    public GenericRepository getRepository(StorageUnit storageUnit){
        return getRepository(storageUnit.getArchetype());
    }

    public GenericRepository getRepository(StorageArchetype archetype){
        return getRepository(archetype.getEntityType());
    }

    public GenericRepository getRepository(EntityType entityType){
        switch (entityType){
            case SINGLETON_ENTITY:
                return SingletonEntityRepositoryHolder.SINGLETON_ENTITY_REPOSITORY;
            case ENTITY_COLLECTION:
                return EntityCollectionRepositoryHolder.ENTITY_COLLECTION_REPOSITORY;
            case SINGLETON_FILE:
                return SingletonFileRepositoryHolder.SINGLETON_FILE_REPOSITORY;
            case FILE_COLLECTION:
                return FileCollectionRepositoryHolder.FILE_COLLECTION_REPOSITORY;
            default:
                return SingletonEntityRepositoryHolder.SINGLETON_ENTITY_REPOSITORY;
        }
    }

    private static class SingletonEntityRepositoryHolder {
        private static final GenericRepository SINGLETON_ENTITY_REPOSITORY = new SingletonEntityRepository();
    }

    private static class EntityCollectionRepositoryHolder {
        private static final GenericRepository ENTITY_COLLECTION_REPOSITORY = new EntityCollectionRepository();
    }

    private static class SingletonFileRepositoryHolder {
        private static final GenericRepository SINGLETON_FILE_REPOSITORY = new SingletonFileRepository();
    }

    private static class FileCollectionRepositoryHolder {
        private static final GenericRepository FILE_COLLECTION_REPOSITORY = new FileCollectionRepository();
    }
}
