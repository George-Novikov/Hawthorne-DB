package com.georgen.hawthorne.repositories;

import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.io.FileOperation;
import com.georgen.hawthorne.model.constants.FileExtension;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.*;
import com.georgen.hawthorne.tools.EntityConverter;
import com.georgen.hawthorne.tools.PathBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileCollectionRepository implements GenericRepository {
    protected FileCollectionRepository(){}
    @Override
    public <C, S> S save(StorageUnit<C, S> storageUnit) throws Exception {
        validateType(storageUnit);
        FileUnit fileUnit = (FileUnit) storageUnit;

        StorageArchetype archetype = fileUnit.getArchetype();

        String entityPath = PathBuilder.getEntityPath(archetype, storageUnit.getGeneratedId());
        String binaryDataPath = PathBuilder.getBinaryDataPath(archetype, storageUnit.getGeneratedId());

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
        if (id == null || id.length == 0) throw new HawthorneException(Message.ID_IS_NULL);

        String entityPath = PathBuilder.getEntityPath(archetype, id[0]);
        T object = null;

        try (FileOperation fileOperation = new FileOperation(entityPath, false)){
            File file = fileOperation.getFile();
            if (!file.exists()) return null;
            object = EntityConverter.convert(file, archetype);
        }

        String binaryDataPath = PathBuilder.getBinaryDataPath(archetype, id[0]);
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
        if (id == null || id.length == 0) throw new HawthorneException(Message.ID_IS_NULL);

        boolean isEntityDeleted = false;
        boolean isBinaryDataDeleted = false;

        String entityPath = PathBuilder.getEntityPath(archetype, id[0]);
        try (FileOperation fileOperation = new FileOperation(entityPath, false)){
            isEntityDeleted = fileOperation.delete();
        }

        String binaryDataPath = PathBuilder.getBinaryDataPath(archetype, id[0]);
        try (FileOperation fileOperation = new FileOperation(binaryDataPath, false)){
            isBinaryDataDeleted = fileOperation.delete();
        }

        return isEntityDeleted && isBinaryDataDeleted;
    }

    @Override
    public <T> List<T> list(StorageArchetype archetype, int limit, int offset) throws Exception {
        Map<String, File> entityFiles = mapRequestedEntityFiles(archetype, limit, offset);
        Map<String, File> binaryDataFiles = mapRequestedBinaryDataFiles(archetype, limit, offset);

        List<T> objects = new ArrayList<>();

        for (Map.Entry<String, File> entry : entityFiles.entrySet()){
            T object = EntityConverter.convert(archetype, entry, binaryDataFiles);
            if (object == null) continue;
            objects.add(object);
        }

        return objects;
    }

    private Map<String, File> mapRequestedEntityFiles(StorageArchetype archetype, int limit, int offset) throws Exception {
        String entityPath = archetype.getPath();
        try (FileOperation fileOperation = new FileOperation(entityPath, false)){
            return fileOperation.mapFilesByExtension(FileExtension.ENTITY_EXTENSION, limit, offset);
        }
    }

    private Map<String, File> mapRequestedBinaryDataFiles(StorageArchetype archetype, int limit, int offset) throws Exception {
        String binaryDataPath = archetype.getPath();
        try (FileOperation fileOperation = new FileOperation(binaryDataPath, false)){
           return fileOperation.mapFilesByExtension(FileExtension.BINARY_DATA_EXTENSION, limit, offset);
        }
    }

    @Override
    public long count(StorageArchetype archetype) throws Exception {
        String path = archetype.getPath();
        try (FileOperation fileOperation = new FileOperation(path, false)){
            return fileOperation.countByExtension(FileExtension.BINARY_DATA_EXTENSION);
        }
    }

    private void validateType(StorageUnit storageUnit) throws HawthorneException {
        boolean isEntityType = storageUnit instanceof FileUnit;
        if (!isEntityType) throw new HawthorneException(Message.STORAGE_UNIT_INCONSISTENCY);
    }
}
