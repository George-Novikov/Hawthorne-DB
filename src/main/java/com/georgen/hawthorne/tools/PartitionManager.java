package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.settings.StorageSettings;

public class PartitionManager {
    public static int locatePartition(StorageArchetype archetype, int id){
        int partitioningThreshold = StorageSettings.getInstance().getPartitioningThreshold();
        return 0;
    }
}
