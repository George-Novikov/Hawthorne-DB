package com.georgen.hawthorne.tools.id.counters;

import com.georgen.hawthorne.model.storage.StorageArchetype;


import java.io.File;
import java.io.IOException;

public abstract class IdCounter<T> {
    public abstract T getNext() throws Exception;
    public abstract long getGenerationsCount() throws Exception;

    public static IdCounter of(StorageArchetype archetype) throws IOException {
        switch (archetype.getIdType()){
            case UUID:
                return new UuidCounter(archetype);
            case INTEGER:
                return new IntegerCounter(archetype);
            case LONG:
                return new LongCounter(archetype);
            default:
                return null;
        }
    }
}