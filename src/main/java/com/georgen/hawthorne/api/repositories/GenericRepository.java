package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;

import java.io.File;

public interface GenericRepository {
    File save(StorageUnit storageUnit);
    <T> T get(StorageArchetype archetype);
}
