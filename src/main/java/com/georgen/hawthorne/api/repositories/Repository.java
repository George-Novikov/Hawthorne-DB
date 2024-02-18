package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.exceptions.FileException;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.settings.StorageSettings;

import java.io.File;

public class Repository {
    private static final RepositoryFactory factory = new RepositoryFactory();

    public static File save(Object object) throws HawthorneException {
        try {
            StorageUnit storageUnit = StorageUnit.of(object);
            GenericRepository repository = factory.getRepository(storageUnit);
            return repository.save(storageUnit);
        } catch (Exception e){
            throw new HawthorneException(e);
        }
    }

    public static <T> T get(Class javaClass){
        StorageSchema schema = StorageSettings.getInstance().getStorageSchema();
        StorageArchetype archetype = schema.get(javaClass.getSimpleName());
        GenericRepository repository = factory.getRepository(archetype);
        return repository.get(archetype);
    }
}
