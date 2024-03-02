package com.georgen.hawthorne.tools.id.counters;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.settings.StorageSettings;
import com.georgen.hawthorne.tools.PathBuilder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UuidCounter extends IdCounter<String> {
    private StorageArchetype archetype;
    private File counterFile;
    private AtomicLong atomicLong;

    public UuidCounter(StorageArchetype archetype) throws IOException {
        this.archetype = archetype;

        this.counterFile = FileFactory.getFile(
                PathBuilder.getIdCounterPath(archetype)
        );

        this.atomicLong = new AtomicLong();
    }

    @Override
    public String getNext() throws Exception {
        try {
            long idCount = getGenerationsCount();
            atomicLong.set(idCount);

            Long incrementedCount = atomicLong.incrementAndGet();
            String nextUuid = UUID.randomUUID().toString();

            updateArchetypePartitionInfo(incrementedCount);
            writeToUuidIndex(nextUuid);
            saveCounterValue(incrementedCount);

            return nextUuid;
        } catch (Exception e) {
            throw new HawthorneException(Message.ID_COUNTER_ERROR);
        }
    }

    @Override
    public long getGenerationsCount() throws Exception {
        String idCount = FileManager.read(counterFile);
        if (idCount == null) idCount = "0";
        return Long.valueOf(idCount);
    }

    private void updateArchetypePartitionInfo(long idCount) throws Exception {
        int partitioningThreshold = StorageSettings.getInstance().getPartitioningThreshold();

        int currentPartition = archetype.getPartitionCounter();
        int targetPartition = idCount > currentPartition * partitioningThreshold ? currentPartition + 1 : currentPartition;

        archetype.setPartitionCounter(targetPartition);
    }

    private void writeToUuidIndex(String uuid) throws Exception {
        String uuidIndexPath = PathBuilder.getUuidIndexPath(archetype);
        File uuidIndexFile = FileFactory.getFile(uuidIndexPath);
        FileManager.append(uuidIndexFile, uuid);
    }

    private void saveCounterValue(Long incrementedCount) throws Exception {
        FileManager.write(counterFile, String.valueOf(incrementedCount));
    }
}
