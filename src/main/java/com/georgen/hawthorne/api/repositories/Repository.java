package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.exceptions.FileException;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;

import java.io.File;

public class Repository {
    private static final RepositoryFactory factory = new RepositoryFactory();

    public static File save(Object object) throws HawthorneException {
        try {
            StorageUnit storageUnit = StorageUnit.of(object);
            StorageArchetype archetype = storageUnit.getArchetype();
            GenericRepository repository = factory.getRepository(archetype);
            return repository.save(object);
        } catch (Exception e){
            throw new HawthorneException(e);
        }
    }


}
