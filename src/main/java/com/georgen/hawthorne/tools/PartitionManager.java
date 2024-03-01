package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.model.storage.StorageUnit;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.tools.id.IdCounterFactory;
import com.georgen.hawthorne.tools.id.counters.IdCounter;

public class PartitionManager {
    public static int locatePartition(StorageArchetype archetype, boolean isNewFile, Object id) throws Exception {
        IdType idType = archetype.getIdType();
        String stringId = String.valueOf(id);

        if (IdType.UUID.equals(idType)){
            return isNewFile ? locateNewPartitionForId(archetype, Long.valueOf(stringId)) : locateExistingPartitionForId(archetype, Long.valueOf(stringId));
        } else {
            return isNewFile ? locateNewPartitionForUuid(archetype) : locateExistingPartitionForUuid(archetype, stringId);
        }
    }

    private static int locateNewPartitionForId(StorageArchetype archetype, long id){
        int partitioningThreshold = StorageSettings.getInstance().getPartitioningThreshold();
        int partitionsCount = archetype.getPartitionsCounter();

        int newPartitionsCount = id > partitioningThreshold * partitionsCount ? partitionsCount + 1 : partitionsCount;
        archetype.setPartitionsCounter(newPartitionsCount);

        return newPartitionsCount;
    }

    private static int locateExistingPartitionForId(StorageArchetype archetype, long id){
        int partitioningThreshold = StorageSettings.getInstance().getPartitioningThreshold();
        int partitionsCount = archetype.getPartitionsCounter();

        int targetPartitionNumber = 0;

        for (int i = 1; i <= partitionsCount; i++){
            if (id < partitioningThreshold * i) targetPartitionNumber = i;
        }

        return targetPartitionNumber;
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

    private static int locateExistingPartitionForUuid(StorageArchetype archetype, String uuid){
        //TODO
    }
}
