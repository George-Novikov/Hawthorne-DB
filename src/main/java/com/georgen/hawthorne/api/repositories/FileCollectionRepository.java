package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;

import java.io.File;

public class FileCollectionRepository implements GenericRepository {
    @Override
    public File save(StorageUnit storageUnit) {
        return null;
    }

    @Override
    public <T> T get(StorageArchetype archetype){
        return null;
    }
}
