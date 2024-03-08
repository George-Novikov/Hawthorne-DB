package com.georgen.hawthorne.repositories;

import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.io.FileOperation;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.*;
import com.georgen.hawthorne.tools.EntityConverter;
import com.georgen.hawthorne.tools.PathBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SingletonFileRepository implements GenericRepository {

    protected SingletonFileRepository(){}

    @Override
    public <C, S> S save(StorageUnit<C, S> storageUnit) throws Exception {
        validateType(storageUnit);
        FileUnit fileUnit = (FileUnit) storageUnit;

        StorageArchetype archetype = fileUnit.getArchetype();

        String entityPath = PathBuilder.getEntityPath(archetype);
        String binaryDataPath = PathBuilder.getBinaryDataPath(archetype);

        try (FileOperation fileOperation = new FileOperation(entityPath, true)){
            File file = fileOperation.getFile();
            FileManager.write(file, fileUnit.getMetadata());
        }

        try (FileOperation fileOperation = new FileOperation(binaryDataPath, true)){
            File file = fileOperation.getFile();
            FileManager.writeBytes(file, fileUnit.getContent());
        }

        return storageUnit.getSource();
    }

    @Override
    public <T, I> T get(StorageArchetype archetype, I... id) throws Exception {
        String entityPath = PathBuilder.getEntityPath(archetype);
        T object = null;

        try (FileOperation fileOperation = new FileOperation(entityPath, false)){
            File file = fileOperation.getFile();
            if (!file.exists()) throw new HawthorneException(Message.NO_ENTITY_FILE);
            object = EntityConverter.convert(file, archetype);
        }

        if (object == null) return null;

        String binaryDataPath = PathBuilder.getBinaryDataPath(archetype);
        byte[] binaryData = null;

        try (FileOperation fileOperation = new FileOperation(binaryDataPath, false)){
            File file = fileOperation.getFile();
            if (!file.exists()) throw new HawthorneException(Message.NO_BINARY_DATA_FILE);
            binaryData = FileManager.readBytes(file);
            if (binaryData == null) throw new HawthorneException(Message.BINARY_DATA_IS_CORRUPTED);
            object = EntityConverter.fillBinaryData(object, binaryData);
        }

        if (object == null) throw new HawthorneException(Message.ENTITY_RETRIEVAL_ERROR);
        return object;
    }

    @Override
    public <I> boolean delete(StorageArchetype archetype, I... id) throws Exception {
        boolean isEntityDeleted = false;
        boolean isBinaryDataDeleted = false;

        String entityPath = PathBuilder.getEntityPath(archetype);
        try (FileOperation fileOperation = new FileOperation(entityPath, false)){
            isEntityDeleted = fileOperation.delete();
        }

        String binaryDataPath = PathBuilder.getBinaryDataPath(archetype);
        try (FileOperation fileOperation = new FileOperation(binaryDataPath, false)){
            isBinaryDataDeleted = fileOperation.delete();
        }

        return isEntityDeleted && isBinaryDataDeleted;
    }

    @Override
    public <T> List<T> list(StorageArchetype archetype, int limit, int offset) throws Exception {
        List<T> list = new ArrayList<>();
        T object = get(archetype);
        if (object != null) list.add(object);
        return list;
    }

    @Override
    public long count(StorageArchetype archetype) throws Exception {
        boolean isExistingEntity = false;
        boolean isExistingBinaryData = false;

        String entityPath = PathBuilder.getEntityPath(archetype);
        try (FileOperation fileOperation = new FileOperation(entityPath, false)){
            isExistingEntity = fileOperation.isExistingFile();
        }

        String binaryDataPath = PathBuilder.getEntityPath(archetype);
        try (FileOperation fileOperation = new FileOperation(binaryDataPath, false)){
            isExistingBinaryData = fileOperation.isExistingFile();
        }

        return isExistingEntity && isExistingBinaryData ? 1 : 0;
    }

    private void validateType(StorageUnit storageUnit) throws HawthorneException {
        boolean isEntityType = storageUnit instanceof FileUnit;
        if (!isEntityType) throw new HawthorneException(Message.STORAGE_UNIT_INCONSISTENCY);
    }
}
