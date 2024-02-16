package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.model.constants.EntityType;

import java.io.File;

public class RepositoryFactory {
    private MonoEntityRepository monoEntityRepository;
    private EntityCollectionRepository entityCollectionRepository;
    private FileRepository fileRepository;

    public GenericRepository getRepository(EntityType entityType){
        switch (entityType){
            case MONO_ENTITY:
                return this.getMonoEntityRepository();
            case ENTITY_COLLECTION:
                return this.getEntityCollectionRepository();
            case FILE:
                return this.getFileRepository();
            default:
                return this.getMonoEntityRepository();
        }
    }

    private MonoEntityRepository getMonoEntityRepository(){
        if (this.monoEntityRepository == null){
            synchronized (RepositoryFactory.class){
                if (this.monoEntityRepository == null){
                    this.monoEntityRepository = new MonoEntityRepository();
                }
            }
        }
        return this.monoEntityRepository;
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

    private FileRepository getFileRepository(){
        if (this.fileRepository == null){
            synchronized (RepositoryFactory.class){
                if (this.fileRepository == null){
                    this.fileRepository = new FileRepository();
                }
            }
        }
        return this.fileRepository;
    }
}
