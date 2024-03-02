package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.tools.id.UuidSearcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PartitionManager {
    public static int locatePartition(StorageArchetype archetype, Object id) throws Exception {
        IdType idType = archetype.getIdType();
        String stringId = String.valueOf(id);

        if (IdType.UUID.equals(idType)){
            return locateExistingPartitionForUuid(archetype, stringId);
        } else {
            Long longId = Long.valueOf(stringId);
            return locateExistingPartitionForId(archetype, longId);
        }
    }

    private static int locateExistingPartitionForId(StorageArchetype archetype, long id){
        int partitioningThreshold = StorageSettings.getInstance().getPartitioningThreshold();
        int partitionsCount = archetype.getPartitionCounter();

        int targetPartitionNumber = 0;

        for (int i = 1; i <= partitionsCount; i++){
            if (id < partitioningThreshold * i){
                targetPartitionNumber = i;
                break;
            }
        }

        return targetPartitionNumber;
    }

    private static int locateExistingPartitionForUuid(StorageArchetype archetype, String uuid) throws IOException {
        int partitionsCount = archetype.getPartitionCounter();

        for (int i = 1; i <= partitionsCount; i++){
            String uuidIndexPath = PathBuilder.getUuidIndexPath(archetype, i);
            File uuidIndexFile = FileFactory.getFile(uuidIndexPath);
            if (uuidIndexFile == null) continue;

            boolean isPresent = UuidSearcher.isUuidPresent(uuidIndexFile, uuid);
            if (isPresent) return i;
        }

        return 0;
    }

    public static boolean isUuidPresent(File uuidIndexFile, String uuid) throws IOException {
        List<String> lines = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(uuidIndexFile));
            lines = reader.lines().collect(Collectors.toList());
            Collections.sort(lines);
            return Collections.binarySearch(lines, uuid) >= 0;
        } finally {
            if (reader != null) reader.close();
        }
    }
}
