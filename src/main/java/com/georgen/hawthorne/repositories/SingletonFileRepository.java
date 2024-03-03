package com.georgen.hawthorne.repositories;

import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.repositories.GenericRepository;

import java.io.File;
import java.util.List;

public class SingletonFileRepository implements GenericRepository {
    protected SingletonFileRepository(){}
    @Override
    public File save(StorageUnit storageUnit){
        return null;
    }

    @Override
    public <T, I> T get(StorageArchetype archetype, I... id){
        return null;
    }

    @Override
    public <I> boolean delete(StorageArchetype archetype, I... id) {
        return false;
    }

    @Override
    public <T> List<T> list(StorageArchetype archetype, int limit, int offset) {
        return null;
    }

    @Override
    public long count(StorageArchetype archetype) {
        return 0;
    }
}
