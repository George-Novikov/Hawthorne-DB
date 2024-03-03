package com.georgen.hawthorne.tools;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.storage.ListRequestScope;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.settings.StorageSettings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PartitionFinder {

    public static int locatePartition(StorageArchetype archetype, Object id) throws Exception {
        if (archetype.getEntityType().isSingleton()) return 0;

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

    private static int locateExistingPartitionForUuid(StorageArchetype archetype, String uuid) throws IOException, HawthorneException {
        int partitionsCount = archetype.getPartitionCounter();

        for (int i = 1; i <= partitionsCount; i++){
            String uuidIndexPath = PathBuilder.getUuidIndexPath(archetype, i);
            File uuidIndexFile = FileFactory.getInstance().getFile(uuidIndexPath, false);
            if (uuidIndexFile == null || uuidIndexFile.exists()) continue;

            boolean isPresent = isUuidPresent(uuidIndexFile, uuid);
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

    public static ListRequestScope getListRequestScope(StorageArchetype archetype, int limit, int offset){
        int partitioningThreshold = StorageSettings.getInstance().getPartitioningThreshold();
        int partitionsCount = archetype.getPartitionCounter();

        int startPartition = locateListStartPartition(partitioningThreshold, partitionsCount, offset);
        int endPartition = locateListEndPartition(partitioningThreshold, partitionsCount, limit, offset);
        int numberOfMiddlePartitions = countNumberOfMiddlePartitions(startPartition, endPartition);
        int startPartitionCount = countStartPartitionObjects(partitioningThreshold, startPartition, offset);
        int endPartitionCount = countEndPartitionObjects(partitioningThreshold, startPartitionCount, limit);

        return new ListRequestScope(
                startPartition,
                startPartitionCount,
                numberOfMiddlePartitions,
                partitioningThreshold,
                endPartition,
                endPartitionCount
        );
    }

    public static int locateListStartPartition(int partitioningThreshold, int partitionsCount, int offset){
        if (offset <= partitioningThreshold) return 1;
        int listStartPartitionNumber = 1;
        for (int i = 1; i <= partitionsCount; i++){
            if (offset <= i * partitioningThreshold){
                listStartPartitionNumber = i;
                break;
            }
        }
        return listStartPartitionNumber;
    }

    public static int locateListEndPartition(int partitioningThreshold, int partitionsCount, int limit, int offset){
        if (limit + offset <= partitioningThreshold) return 1;
        int listEndPartitionNumber = 1;
        for (int i = 1; i <= partitionsCount; i++){
            if (limit + offset <= i * partitioningThreshold){
                listEndPartitionNumber = i;
                break;
            }
        }
        return listEndPartitionNumber;
    }

    public static int countNumberOfMiddlePartitions(int startPartition, int endPartition){
        int count = endPartition - startPartition - 1;
        return count < 0 ? 0 : count;
    }

    public static int countStartPartitionObjects(int partitioningThreshold, int startPartitionNumber, int offset){
        return (partitioningThreshold * startPartitionNumber) - offset;
    }

    public static int countEndPartitionObjects(int partitioningThreshold, int startPartitionCount, int limit){
        int count = limit - startPartitionCount;
        while (count < partitioningThreshold){
            count = count - partitioningThreshold;
        }
        return count;
    }

}
