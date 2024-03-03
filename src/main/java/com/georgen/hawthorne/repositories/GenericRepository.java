package com.georgen.hawthorne.repositories;

import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface GenericRepository {
    <C, S> S save(StorageUnit<C, S> storageUnit) throws Exception;
    <T, I> T get(StorageArchetype archetype, I... id) throws Exception;
    <I> boolean delete(StorageArchetype archetype, I... id) throws Exception;
    <T> List<T> list(StorageArchetype archetype, int limit, int offset) throws Exception;
    long count(StorageArchetype archetype) throws Exception;

}
