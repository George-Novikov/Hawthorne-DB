package com.georgen.hawthorne.tools.id;

import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.tools.id.counters.IdCounter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class IdCounterFactory {
    private ConcurrentMap<String, IdCounter> idCounters;

    private IdCounterFactory() {
        this.idCounters = new ConcurrentHashMap();
    }

    public IdCounter getCounter(StorageArchetype archetype) throws IOException, HawthorneException {
        IdCounter idCounter = idCounters.get(archetype.getSimpleName());
        if (idCounter != null) return idCounter;

        idCounter = IdCounter.of(archetype);
        idCounters.put(
                archetype.getSimpleName(),
                idCounter
        );

        return idCounter;
    }

    public static IdCounterFactory getInstance() throws IOException {
        return IdCounterFactoryHolder.INSTANCE;
    }

    private static class IdCounterFactoryHolder {
        private static final IdCounterFactory INSTANCE = new IdCounterFactory();
    }
}
