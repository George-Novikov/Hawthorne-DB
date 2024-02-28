package com.georgen.hawthorne.tools.id;

import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.tools.id.counters.IdCounter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class IdCounterFactory {
    private ConcurrentMap idCounters;

    private IdCounterFactory() {
        this.idCounters = new ConcurrentHashMap();
    }

    public static IdCounterFactory getInstance() throws IOException {
        IdCounterFactory instance = IdCounterFactoryHolder.INSTANCE;
        instance.populate();
        return instance;
    }

    private void populate() throws IOException {
        ConcurrentMap<String, StorageArchetype> archetypes = StorageSettings.getInstance().getStorageSchema().getSchema();

        for (Map.Entry<String, StorageArchetype> entry : archetypes.entrySet()){
            StorageArchetype archetype = entry.getValue();
            if (archetype.getEntityType().isEntity()) continue;

            IdCounter idCounter = IdCounter.of(archetype);
            if (idCounter == null) continue;

            idCounters.put(
                    archetype.getSimpleName(),
                    idCounter
            );
        }
    }

    private static class IdCounterFactoryHolder {
        private static final IdCounterFactory INSTANCE = new IdCounterFactory();
    }


}
