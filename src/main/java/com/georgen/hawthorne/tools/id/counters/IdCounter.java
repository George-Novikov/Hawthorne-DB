package com.georgen.hawthorne.tools.id.counters;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.constants.SystemProperty;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.tools.PathBuilder;

import java.io.File;
import java.io.IOException;

public abstract class IdCounter<T> {
    public abstract T getNext();

    public static IdCounter of(StorageArchetype archetype) throws IOException {
        switch (archetype.getIdType()){
            case UUID:
                return new UuidCounter();
            case INTEGER:
                return new IntegerCounter(archetype);
            case LONG:
                return new LongCounter(archetype);
            default:
                return null;
        }
    }
}
