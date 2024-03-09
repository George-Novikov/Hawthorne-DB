package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.georgen.hawthorne.io.FileIOManager;
import com.georgen.hawthorne.tools.Serializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StorageSchema {
    private File controlFile;
    private ConcurrentMap<String, StorageArchetype> schema;
    public StorageSchema(){
        this.schema = new ConcurrentHashMap<>();
    };
    public StorageSchema(File controlFile){
        this.controlFile = controlFile;
        this.schema = new ConcurrentHashMap<>();
    }

    @JsonIgnore
    public File getControlFile() {
        return controlFile;
    }

    public void setControlFile(File controlFile) {
        this.controlFile = controlFile;
    }

    @JsonProperty("schema")
    public ConcurrentMap<String, StorageArchetype> getSchema() {
        return schema;
    }

    @JsonProperty("schema")
    public void setSchema(ConcurrentMap<String, StorageArchetype> schema) {
        this.schema = schema;
    }

    public void update(StorageArchetype archetype) throws Exception {
        if (archetype == null) return;
        if (isNew(archetype)) register(archetype);
    }

    public StorageArchetype get(String simpleName){
        return this.schema.get(simpleName);
    }

    public void consumeLastId(StorageArchetype archetype){

    }

    private void save() throws Exception {
        synchronized (this){
            String storageSchemaJson = Serializer.toJson(this);
            FileIOManager.write(controlFile, storageSchemaJson);
        }
    }

    public StorageArchetype register(StorageArchetype archetype) throws Exception {
        StorageArchetype addedArchetype = this.schema.put(
                archetype.getSimpleName(),
                archetype
        );
        save();
        return addedArchetype;
    }

    public StorageArchetype unregister(Class javaClass) throws Exception {
        StorageArchetype existingArchetype = this.schema.get(javaClass.getSimpleName());
        if (existingArchetype == null) return null;
        return unregister(existingArchetype);
    }

    private StorageArchetype unregister(StorageArchetype archetype) throws Exception {
        StorageArchetype removedArchetype = this.schema.remove(archetype.getSimpleName());
        cleanUpResidualFiles(removedArchetype);
        save();
        return removedArchetype;
    }

    private void cleanUpResidualFiles(StorageArchetype archetype) throws IOException {
        String path = archetype.getPath();

        try (Stream<Path> pathStream = Files.walk(Paths.get(path))){
            pathStream
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    private boolean isNew(StorageArchetype archetype){
        StorageArchetype existingArchetype = this.schema.get(archetype.getSimpleName());
        return existingArchetype == null || !archetype.equals(existingArchetype);
    }
}
