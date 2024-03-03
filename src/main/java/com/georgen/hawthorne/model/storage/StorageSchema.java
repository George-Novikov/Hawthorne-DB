package com.georgen.hawthorne.model.storage;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.tools.Serializer;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StorageSchema {
    private File controlFile;
    private ConcurrentMap<String, StorageArchetype> schema;
    public StorageSchema(File controlFile){
        this.controlFile = controlFile;
        this.schema = new ConcurrentHashMap<>();
    }

    public ConcurrentMap<String, StorageArchetype> getSchema() {
        return schema;
    }

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
            FileManager.write(controlFile, storageSchemaJson);
        }
    }

    private StorageArchetype register(StorageArchetype archetype) throws Exception {
        StorageArchetype addedArchetype = this.schema.put(
                archetype.getSimpleName(),
                archetype
        );
        save();
        return addedArchetype;
    }

    private StorageArchetype unregister(StorageArchetype archetype) throws Exception {
        StorageArchetype removedArchetype = this.schema.remove(archetype.getSimpleName());
        save();
        return removedArchetype;
    }

    private boolean isNew(StorageArchetype archetype){
        StorageArchetype existingArchetype = this.schema.get(archetype.getSimpleName());
        return existingArchetype == null || !archetype.equals(existingArchetype);
    }
}
