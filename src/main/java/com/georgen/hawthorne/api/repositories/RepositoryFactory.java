package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.storage.StorageArchetype;

public class RepositoryFactory {
    private SingletonEntityRepository singletonEntityRepository;
    private EntityCollectionRepository entityCollectionRepository;
    private SingletonFileRepository singletonFileRepository;

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
            default:
                return this.getSingletonEntityRepository();
        }
    }

    private SingletonEntityRepository getSingletonEntityRepository(){
        if (this.singletonEntityRepository == null){
            synchronized (RepositoryFactory.class){
                if (this.singletonEntityRepository == null){
                    this.singletonEntityRepository = new SingletonEntityRepository();
                }
            }
        }
        return this.singletonEntityRepository;
    }

    private EntityCollectionRepository getEntityCollectionRepository(){
        if (this.entityCollectionRepository == null){
            synchronized (RepositoryFactory.class){
                if (this.entityCollectionRepository == null){
                    this.entityCollectionRepository = new EntityCollectionRepository();
                }
            }
        }
        return this.entityCollectionRepository;
    }

    private SingletonFileRepository getSingletonFileRepository(){
        if (this.singletonFileRepository == null){
            synchronized (RepositoryFactory.class){
                if (this.singletonFileRepository == null){
                    this.singletonFileRepository = new SingletonFileRepository();
                }
            }
        }
        return this.singletonFileRepository;
    }
}
