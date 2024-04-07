package com.georgen.hawthorne.repositories;

import com.georgen.hawthorne.io.FileIOManager;
import com.georgen.hawthorne.io.FileOperation;
import com.georgen.hawthorne.model.constants.FileExtension;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.*;
import com.georgen.hawthorne.tools.EntityConverter;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.tools.paths.PathBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EntityCollectionRepository implements GenericRepository{

    protected EntityCollectionRepository(){}

    @Override
    public <C, S> S save(StorageUnit<C, S> storageUnit) throws Exception {
        validateType(storageUnit);
        EntityUnit entityUnit = (EntityUnit) storageUnit;

        StorageArchetype archetype = entityUnit.getArchetype();
        StorageSchema storageSchema = StorageSettings.getInstance().getStorageSchema();
        storageSchema.update(archetype);

        String path = PathBuilder.getEntityPath(archetype, storageUnit.getSourceId());

        try (FileOperation fileOperation = new FileOperation(path, true)){
            File file = fileOperation.getFile();
            FileIOManager.write(file, entityUnit.getContent());
        }

        return storageUnit.getSource();
    }

    @Override
    public <T, I> T get(StorageArchetype archetype, I... id) throws Exception {
        if (id == null || id.length == 0) throw new HawthorneException(Message.ID_IS_NULL);

        String path = PathBuilder.getEntityPath(archetype, id[0]);
        try (FileOperation fileOperation = new FileOperation(path, false)){
            File file = fileOperation.getFile();
            return EntityConverter.convert(file, archetype);
        }
    }

    @Override
    public <I> boolean delete(StorageArchetype archetype, I... id) throws Exception {
        if (id == null || id.length == 0) throw new HawthorneException(Message.ID_IS_NULL);

        String path = PathBuilder.getEntityPath(archetype, id[0]);
        try (FileOperation fileOperation = new FileOperation(path, false)){
            return fileOperation.delete();
        }
    }

    @Override
    public long count(StorageArchetype archetype) throws Exception {
        String path = archetype.getPath();
        try (FileOperation fileOperation = new FileOperation(path, false)){
            return fileOperation.countByExtension(FileExtension.ENTITY_EXTENSION);
        }
    }

    @Override
    public <T> List<T> list(StorageArchetype archetype, int limit, int offset) throws Exception {
        List<File> files = listRequestedFiles(archetype, limit, offset);
        List<T> objects = new ArrayList<>();

        for (File file : files){
            objects.add(
                    EntityConverter.convert(file, archetype)
            );
        }

        return objects;
    }

    private List<File> listRequestedFiles(StorageArchetype archetype, int limit, int offset) throws Exception {
        String path = archetype.getPath();
        try (FileOperation fileOperation = new FileOperation(path, false)){
            return fileOperation.listFilesByExtension(
                    FileExtension.ENTITY_EXTENSION,
                    limit,
                    offset
            );
        }
    }

    private void validateType(StorageUnit storageUnit) throws HawthorneException {
        boolean isEntityType = storageUnit instanceof EntityUnit;
        if (!isEntityType) throw new HawthorneException(Message.STORAGE_UNIT_INCONSISTENCY);
    }
}
