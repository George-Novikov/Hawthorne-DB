package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;

import java.io.File;
import java.util.List;

public class SingletonFileRepository implements GenericRepository {
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
    public <T> List<T> list(StorageArchetype archetype) {
        return null;
    }

    @Override
    public <T> long count(StorageArchetype archetype) {
        return 0;
    }
}
