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
    private File counterFile;
    private File uuidIndexFile;
    private AtomicLong atomicLong;
    private int partitionNumber;

    public UuidCounter(StorageArchetype archetype) throws IOException {
        this.counterFile = FileFactory.getFile(
                PathBuilder.getIdCounterPath(archetype)
        );

        this.uuidIndexFile = FileFactory.getFile(
                PathBuilder.getUuidIndexPath(archetype)
        );

        this.atomicLong = new AtomicLong();
        this.partitionNumber = archetype.getPartitionsCounter();
    }

    @Override
    public String getNext() throws Exception {
        try {
            String nextValue = UUID.randomUUID().toString();

            long idCount = getGenerationsCount();
            int partitioningThreshold = StorageSettings.getInstance().getPartitioningThreshold();
            int targetPartitionNumber = idCount > partitionNumber * partitioningThreshold ? partitionNumber + 1 : partitionNumber;


            atomicLong.set(idCount);
            Long incrementedLong = atomicLong.incrementAndGet();
            FileManager.write(counterFile, String.valueOf(incrementedLong));

            return nextValue;
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
}
