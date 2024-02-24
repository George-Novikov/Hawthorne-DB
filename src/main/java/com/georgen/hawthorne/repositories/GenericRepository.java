package com.georgen.hawthorne.repositories;

import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;

import java.io.File;
import java.util.List;

public interface GenericRepository {
    <C, S> S save(StorageUnit<C, S> storageUnit);
    <T, I> T get(StorageArchetype archetype, I... id);
    <I> boolean delete(StorageArchetype archetype, I... id);
    <T> List<T> list(StorageArchetype archetype);
    long count(StorageArchetype archetype);

}
