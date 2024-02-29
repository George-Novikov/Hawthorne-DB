package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.tools.id.IdCounterFactory;
import com.georgen.hawthorne.tools.id.counters.IdCounter;
import com.georgen.hawthorne.tools.id.counters.UuidCounter;

import java.io.File;
import java.io.IOException;

public class PartitionManager {
    public static int locatePartition(StorageUnit storageUnit, boolean isNewFile) throws Exception {
        StorageArchetype archetype = storageUnit.getArchetype();
        Object generatedId = storageUnit.getGeneratedId();
        IdType idType = archetype.getIdType();

        if (IdType.UUID.equals(idType)){
            return isNewFile ? locateNewPartitionForUuid(archetype) : locateExistingPartitionForId();
        } else {
            return isNewFile ? locateNewPartitionForId(archetype, (long) generatedId) : locateExistingPartitionForUuid();
        }
    }

    private static int locateNewPartitionForId(StorageArchetype archetype, long id){
        int partitioningThreshold = StorageSettings.getInstance().getPartitioningThreshold();
        int partitionsCount = archetype.getPartitionsCounter();

        int newPartitionsCount = id > partitioningThreshold * partitionsCount ? partitionsCount + 1 : partitionsCount;
        archetype.setPartitionsCounter(newPartitionsCount);

        return newPartitionsCount;
    }

    private static int locateExistingPartitionForId(){
        //TODO
    }

    private static int locateNewPartitionForUuid(StorageArchetype archetype) throws Exception {
        int partitioningThreshold = StorageSettings.getInstance().getPartitioningThreshold();
        int partitionsCount = archetype.getPartitionsCounter();

        IdCounter idCounter = IdCounterFactory.getInstance().getCounter(archetype);
        long generationsCount = idCounter.getGenerationsCount();

        int newPartitionsCount = generationsCount > partitioningThreshold * partitionsCount ? partitionsCount + 1 : partitionsCount;
        archetype.setPartitionsCounter(newPartitionsCount);

        return newPartitionsCount;
    }

    private static int locateExistingPartitionForUuid(){
        //TODO
    }
}
