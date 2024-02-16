package com.georgen.hawthorne.api.repositories;

import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.exceptions.FileException;

import java.io.File;

public class Repository {
    private static final RepositoryFactory factory = new RepositoryFactory();

    public static File save(Object object) throws FileException {
        Class javaClass = object.getClass();
        EntityType entityType = EntityType.of(javaClass);
        GenericRepository repository = factory.getRepository(entityType);
        return repository.save(object);
    }


}
