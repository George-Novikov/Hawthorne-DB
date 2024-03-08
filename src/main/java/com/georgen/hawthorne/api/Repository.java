package com.georgen.hawthorne.api;

import com.georgen.hawthorne.model.constants.EntityType;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.repositories.GenericRepository;
import com.georgen.hawthorne.repositories.RepositoryFactory;
import com.georgen.hawthorne.settings.StorageSettings;

import java.util.List;

public class Repository {
    private static final RepositoryFactory factory = new RepositoryFactory();
    private static StorageSchema schema;

    public static <T> T save(T object) throws HawthorneException {
        try {
            StorageArchetype archetype = getArchetype(object.getClass());
            StorageUnit storageUnit = archetype != null ? StorageUnit.of(archetype, object) : StorageUnit.of(object);
            GenericRepository repository = factory.getRepository(storageUnit);
            repository.save(storageUnit);
            updateSchemaArchetype(storageUnit.getArchetype());
            return object;
        } catch (Exception e){
            throw new HawthorneException(e);
        }
    }

    public static <T, I> T get(Class javaClass, I... id) throws Exception {
        StorageArchetype archetype = getArchetype(javaClass);
        if (archetype == null) return null;

        GenericRepository repository = factory.getRepository(archetype);
        T object = repository.get(archetype, id);

        return object;
    }

    public static <I> boolean delete(Class javaClass, I... id) throws Exception {
        StorageArchetype archetype = getArchetype(javaClass);
        if (archetype == null) throw new HawthorneException(Message.DELETE_FAIL);

        GenericRepository repository = factory.getRepository(archetype);
        return repository.delete(archetype, id);
    }

    public static <T> List<T> list(Class javaClass, int limit, int offset) throws Exception {
        StorageArchetype archetype = getArchetype(javaClass);
        if (archetype == null) return null;

        GenericRepository repository = factory.getRepository(archetype);
        return repository.list(archetype, limit, offset);
    }

    public static long count(Class javaClass) throws Exception {
        StorageArchetype archetype = getArchetype(javaClass);
        if (archetype == null) return 0;

        GenericRepository repository = factory.getRepository(archetype);
        return repository.count(archetype);
    }

    private static StorageArchetype getArchetype(Class javaClass) throws HawthorneException {
        if (!EntityType.isTyped(javaClass)) throw new HawthorneException(Message.NOT_ANNOTATED);
        initSchemaOrBypass();
        return schema.get(javaClass.getSimpleName());
    }

    private static void updateSchemaArchetype(StorageArchetype archetype) throws Exception {
        initSchemaOrBypass();
        schema.update(archetype);
    }

    private static void initSchemaOrBypass(){
        if (schema == null){
            synchronized (Repository.class){
                if (schema == null){
                    schema = StorageSettings.getInstance().getStorageSchema();
                }
            }
        }
    }
}
