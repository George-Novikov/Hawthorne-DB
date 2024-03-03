package com.georgen.hawthorne.repositories;

import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.io.FileOperation;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.*;
import com.georgen.hawthorne.tools.PartitionFinder;
import com.georgen.hawthorne.tools.Serializer;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.tools.PathBuilder;
import com.georgen.hawthorne.tools.logging.SelfTracking;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EntityCollectionRepository<I> implements GenericRepository, SelfTracking {
    protected EntityCollectionRepository(){}
    @Override
    public <C, S> S save(StorageUnit<C, S> storageUnit) throws Exception {
        validateType(storageUnit);

        EntityUnit entityUnit = (EntityUnit) storageUnit;
        StorageArchetype archetype = entityUnit.getArchetype();

        StorageSchema storageSchema = StorageSettings.getInstance().getStorageSchema();
        storageSchema.update(archetype);

        String path = PathBuilder.getEntityPath(archetype, storageUnit.getGeneratedId(), storageUnit.isNew());
        try (FileOperation fileOperation = new FileOperation(path, true)){
            File file = fileOperation.getFile();
            FileManager.write(file, entityUnit.getContent());
        }

        return storageUnit.getSource();
    }

    @Override
    public <T, I> T get(StorageArchetype archetype, I... id) throws Exception {
        if (id == null || id.length == 0) throw new HawthorneException(Message.ID_IS_NULL);

        String path = PathBuilder.getEntityPath(archetype, id[0], false);
        try (FileOperation fileOperation = new FileOperation(path, false)){
            File file = fileOperation.getFile();

            String json = FileManager.read(file);
            if (json == null || json.isEmpty()) throw new HawthorneException(Message.FILE_IS_CORRUPTED);

            Class javaClass = Class.forName(archetype.getFullName());
            T object = Serializer.deserialize(json, javaClass);
            if (object == null) throw new HawthorneException(Message.ENTITY_RETRIEVAL_ERROR);

            return object;
        }
    }

    @Override
    public <I> boolean delete(StorageArchetype archetype, I... id) throws Exception {
        if (id == null || id.length == 0) throw new HawthorneException(Message.ID_IS_NULL);

        String path = PathBuilder.getEntityPath(archetype, id[0], false);
        try (FileOperation fileOperation = new FileOperation(path, false)){
            return fileOperation.delete();
        }
    }

    @Override
    public <T> List<T> list(StorageArchetype archetype, int limit, int offset) throws Exception {
        int partitionCount = archetype.getPartitionCounter();

        List<T> objects = new ArrayList<>();
        List<File> entityFiles = new ArrayList<>();
        ListRequestScope listRequestScope = PartitionFinder.getListRequestScope(archetype, limit, offset);

        for (int i = 1; i <= partitionCount; i++){
            String path = PathBuilder.concatenate(archetype.getPath(), i);
            try (FileOperation fileOperation = new FileOperation(path, true)){
                List<File> partitionFiles = fileOperation.listBinaryFiles(limit, offset);
                entityFiles.addAll(partitionFiles);
            }
        }

        return null;
    }

    @Override
    public long count(StorageArchetype archetype) {
        return 0;
    }

    private void validateType(StorageUnit storageUnit) throws HawthorneException {
        boolean isEntityType = storageUnit instanceof EntityUnit;
        if (!isEntityType) throw new HawthorneException(Message.NOT_AN_ENTITY);
    }
}
