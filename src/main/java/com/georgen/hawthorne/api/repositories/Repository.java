package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.FileMessage;
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

    public static <T, I> T get(Class javaClass, I... id) throws HawthorneException {
        if (!EntityType.isTyped(javaClass)) throw new HawthorneException(FileMessage.NOT_COMPATIBLE);

        StorageSchema schema = StorageSettings.getInstance().getStorageSchema();
        StorageArchetype archetype = schema.get(javaClass.getSimpleName());
        if (archetype == null) return null;

        GenericRepository repository = factory.getRepository(archetype);
        return repository.get(archetype, id);
    }

    public static <I> boolean delete(Class javaClass, I... id) throws HawthorneException {
        if (!EntityType.isTyped(javaClass)) throw new HawthorneException(FileMessage.NOT_COMPATIBLE);

        StorageSchema schema = StorageSettings.getInstance().getStorageSchema();
        StorageArchetype archetype = schema.get(javaClass.getSimpleName());
        if (archetype == null) throw new HawthorneException(FileMessage.DELETE_FAIL);

        GenericRepository repository = factory.getRepository(archetype);
        return repository.delete(archetype, id);
    }
}
