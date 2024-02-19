package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;

import java.io.File;
import java.util.List;

public interface GenericRepository {
    File save(StorageUnit storageUnit);
    <T, I> T get(StorageArchetype archetype, I... id);
//    <I> boolean delete(StorageArchetype archetype, I... id);
//    <T> long count(StorageArchetype archetype);
//    <T> List<T> list(StorageArchetype archetype);
//    <T, K> List<T> search(StorageArchetype archetype, K searchValue);
}
