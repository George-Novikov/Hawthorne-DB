package com.georgen.hawthorne.repositories.handlers;

import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;

import java.io.IOException;

public abstract class StorageUnitManager<U extends StorageUnit> {
    public abstract U save(U storageUnit) throws Exception;
    public abstract <T, I> T get(StorageArchetype archetype, I... id) throws Exception;
    public abstract <I> boolean delete(StorageArchetype archetype, I... id) throws HawthorneException, IOException;
}
