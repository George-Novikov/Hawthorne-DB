package com.georgen.hawthorne.repositories.handlers;

import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.storage.FileUnit;
import com.georgen.hawthorne.model.storage.StorageArchetype;

import java.io.IOException;

public class FileUnitManager extends StorageUnitManager<FileUnit>{
    @Override
    public FileUnit save(FileUnit storageUnit) throws Exception {
        return null;
    }

    @Override
    public <T, I> T get(StorageArchetype archetype, I... id) throws Exception {
        return null;
    }

    @Override
    public <I> boolean delete(StorageArchetype archetype, I... id) throws HawthorneException, IOException {
        return false;
    }
}
