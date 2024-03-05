package com.georgen.hawthorne.repositories;

import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.io.FileOperation;
import com.georgen.hawthorne.model.constants.FileExtension;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.*;
import com.georgen.hawthorne.tools.EntityConverter;
import com.georgen.hawthorne.tools.PartitionFinder;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.tools.PathBuilder;
import com.georgen.hawthorne.tools.logging.SelfTracking;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class EntityCollectionRepository implements GenericRepository, SelfTracking {
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
            return EntityConverter.convert(file, archetype);
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
    public long count(StorageArchetype archetype) throws IOException {
        int partitionsCount = archetype.getPartitionCounter();
        long count = 0;

        for (int i = 1; i <= partitionsCount; i++){
            String path = PathBuilder.concatenate(archetype.getPath(), i);
            try (Stream<Path> files = Files.list(Paths.get(path))){
                count += files.count();
            }
        }

        return count;
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
        ListRequestScope listRequestScope = PartitionFinder.getListRequestScope(archetype, limit, offset);

        List<File> files = listStartPartitionFiles(archetype, listRequestScope);
        if (listRequestScope.hasMiddlePartitions()){
            files.addAll(listMiddlePartitionsFiles(archetype, listRequestScope));
        }
        if (listRequestScope.hasEndPartition()){
            files.addAll(listEndPartitionFiles(archetype, listRequestScope));
        }

        return files;
    }

    private List<File> listStartPartitionFiles(StorageArchetype archetype, ListRequestScope listRequestScope) throws Exception {
        if (!listRequestScope.hasStartPartition()) return new ArrayList<>();

        String path = PathBuilder.concatenate(archetype.getPath(), listRequestScope.getStartPartition());
        try (FileOperation fileOperation = new FileOperation(path, false)){
            return fileOperation.listFilesByExtension(
                    FileExtension.ENTITY_FILE_EXTENSION,
                    listRequestScope.getStartPartitionCount(),
                    listRequestScope.getStartPartitionOffset() //TODO: this offset is still wrong - recalculate
            );
        }
    }

    private List<File> listMiddlePartitionsFiles(StorageArchetype archetype, ListRequestScope listRequestScope) throws Exception {
        List<File> files = new ArrayList<>();

        if (!listRequestScope.hasMiddlePartitions()) return files;

        int start = listRequestScope.getStartPartition() + 1;
        int end = listRequestScope.getStartPartition() + listRequestScope.getNumberOfMiddlePartitions();

        for (int i = start; i <= end; i++){
            String path = PathBuilder.concatenate(archetype.getPath(), i);
            try (FileOperation fileOperation = new FileOperation(path, false)){
                List<File> partitionFiles = fileOperation.listFilesByExtension(
                        FileExtension.ENTITY_FILE_EXTENSION,
                        listRequestScope.getSizeOfMiddlePartitions(),
                        0
                );
                files.addAll(partitionFiles);
            }
        }
        return files;
    }

    private List<File> listEndPartitionFiles(StorageArchetype archetype, ListRequestScope listRequestScope) throws Exception{
        if (!listRequestScope.hasEndPartition()) return new ArrayList<>();

        String path = PathBuilder.concatenate(archetype.getPath(), listRequestScope.getEndPartition());
        try (FileOperation fileOperation = new FileOperation(path, false)){
            return fileOperation.listFilesByExtension(
                    FileExtension.ENTITY_FILE_EXTENSION,
                    listRequestScope.getEndPartitionCount(),
                    0
            );
        }
    }

    private void validateType(StorageUnit storageUnit) throws HawthorneException {
        boolean isEntityType = storageUnit instanceof EntityUnit;
        if (!isEntityType) throw new HawthorneException(Message.NOT_AN_ENTITY);
    }
}
