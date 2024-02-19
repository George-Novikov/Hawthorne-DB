package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.serialization.Serializer;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StorageSchema {
    private File controlFile;
    private Map<String, StorageArchetype> schema;
    public StorageSchema(File controlFile){
        this.controlFile = controlFile;
        this.schema = new ConcurrentHashMap<>();
    }

    public Map<String, StorageArchetype> getSchema() {
        return schema;
    }

    public void setSchema(Map<String, StorageArchetype> schema) {
        this.schema = schema;
    }

    public void update(StorageArchetype archetype) throws Exception {
        if (archetype == null) return;
        if (isNew(archetype)) register(archetype);
    }

    public StorageArchetype get(String simpleName){
        return this.schema.get(simpleName);
    }

    public StorageArchetype remove(StorageArchetype archetype){
        synchronized (StorageSchema.class){
            return this.schema.remove(archetype.getSimpleName());
        }
    }

    public void save() throws Exception {
        String storageSchemaJson = Serializer.toJson(this);
        FileManager.write(controlFile, storageSchemaJson);
    }

    private void register(StorageArchetype archetype) throws Exception {
        this.schema.put(
                archetype.getSimpleName(),
                archetype
        );

        synchronized (StorageSchema.class){
            save();
        }
    }

    private boolean isNew(StorageArchetype archetype){
        StorageArchetype existingArchetype = this.schema.get(archetype.getSimpleName());
        return existingArchetype == null || !archetype.equals(existingArchetype);
    }
}
