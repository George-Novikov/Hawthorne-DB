package com.georgen.hawthorne.repositories;

import com.georgen.hawthorne.io.FileOperation;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.EntityUnit;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.tools.PathBuilder;
import com.georgen.hawthorne.tools.EntityConverter;
import com.georgen.hawthorne.tools.logging.SelfTracking;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SingletonEntityRepository implements GenericRepository, SelfTracking {

    protected SingletonEntityRepository(){}

    @Override
    public <C, S> S save(StorageUnit<C, S> storageUnit) throws Exception {
        validateType(storageUnit);
        EntityUnit entityUnit = (EntityUnit) storageUnit;

        StorageArchetype archetype = entityUnit.getArchetype();
        StorageSchema storageSchema = StorageSettings.getInstance().getStorageSchema();
        storageSchema.update(archetype);

        String path = PathBuilder.getEntityPath(archetype, storageUnit.getGeneratedId());

        try (FileOperation fileOperation = new FileOperation(path, true)){
            File file = fileOperation.getFile();
            FileManager.write(file, entityUnit.getContent());
        }

        return storageUnit.getSource();
    }

    @Override
    public <T, I> T get(StorageArchetype archetype, I... id) throws Exception {
        String path = PathBuilder.getEntityPath(archetype);

        try (FileOperation fileOperation = new FileOperation(path, false)){
            File file = fileOperation.getFile();
            return EntityConverter.convert(file, archetype);
        }
    }

    @Override
    public <I> boolean delete(StorageArchetype archetype, I... id) throws Exception {
        String path = PathBuilder.getEntityPath(archetype);

        try (FileOperation fileOperation = new FileOperation(path, false)){
            return fileOperation.delete();
        }
    }

    @Override
    public <T> List<T> list(StorageArchetype archetype, int limit, int offset) throws Exception {
        List<T> list = new ArrayList<>();
        T object = get(archetype);
        if (object != null) list.add(object);
        return list;
    }

    public long count(StorageArchetype archetype) throws Exception {
        String path = PathBuilder.getEntityPath(archetype);
        try (FileOperation fileOperation = new FileOperation(path, false)){
            return fileOperation.isExistingFile() ? 1 : 0;
        }
    }

    private void validateType(StorageUnit storageUnit) throws HawthorneException {
        boolean isEntityType = storageUnit instanceof EntityUnit;
        if (!isEntityType) throw new HawthorneException(Message.STORAGE_UNIT_INCONSISTENCY);
    }
}
