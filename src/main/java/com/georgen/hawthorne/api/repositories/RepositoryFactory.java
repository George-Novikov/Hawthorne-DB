package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;

public class RepositoryFactory {
    private GenericRepository singletonEntityRepository;
    private GenericRepository entityCollectionRepository;
    private GenericRepository singletonFileRepository;
    private GenericRepository fileCollectionRepository;

    public GenericRepository getRepository(StorageUnit storageUnit){
        return getRepository(storageUnit.getArchetype());
    }

    public GenericRepository getRepository(StorageArchetype archetype){
        return getRepository(archetype.getEntityType());
    }

    public GenericRepository getRepository(EntityType entityType){
        switch (entityType){
            case SINGLETON_ENTITY:
                return this.getSingletonEntityRepository();
            case ENTITY_COLLECTION:
                return this.getEntityCollectionRepository();
            case SINGLETON_FILE:
                return this.getSingletonFileRepository();
            case FILE_COLLECTION:
                return this.getFileCollectionRepository();
            default:
                return this.getSingletonEntityRepository();
        }
    }

    private GenericRepository getSingletonEntityRepository(){
        if (this.singletonEntityRepository == null){
            synchronized (RepositoryFactory.class){
                if (this.singletonEntityRepository == null){
                    this.singletonEntityRepository = new SingletonEntityRepository();
                }
            }
        }
        return this.singletonEntityRepository;
    }

    private GenericRepository getEntityCollectionRepository(){
        if (this.entityCollectionRepository == null){
            synchronized (RepositoryFactory.class){
                if (this.entityCollectionRepository == null){
                    this.entityCollectionRepository = new EntityCollectionRepository();
                }
            }
        }
        return this.entityCollectionRepository;
    }

    private GenericRepository getSingletonFileRepository(){
        if (this.singletonFileRepository == null){
            synchronized (RepositoryFactory.class){
                if (this.singletonFileRepository == null){
                    this.singletonFileRepository = new SingletonFileRepository();
                }
            }
        }
        return this.singletonFileRepository;
    }

    private GenericRepository getFileCollectionRepository(){
        if (this.fileCollectionRepository == null){
            synchronized (RepositoryFactory.class){
                if (this.fileCollectionRepository == null){
                    this.fileCollectionRepository = new FileCollectionRepository();
                }
            }
        }
        return this.fileCollectionRepository;
    }
}
