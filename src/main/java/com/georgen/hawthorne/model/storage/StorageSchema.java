package com.georgen.hawthorne.model.storage;

import com.georgen.hawthorne.config.Settings;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.serialization.Serializer;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class StorageSchema {
    private Set<StorageArchetype> units;
    public StorageSchema(){
        this.units = new HashSet<>();
    }

    public Set<StorageArchetype> getUnits() {
        return units;
    }

    public void setUnits(Set<StorageArchetype> units) {
        this.units = units;
    }

    public void register(StorageArchetype unit){
        this.units.add(unit);
    }

    public boolean remove(StorageArchetype unit){
        return this.units.remove(unit);
    }

    public void save() throws Exception {
        String storageSchemaJson = Serializer.toJson(this);
        File controlFile = Settings.getInstance().getControlFile();
        FileManager.write(controlFile, storageSchemaJson);
    }
}
