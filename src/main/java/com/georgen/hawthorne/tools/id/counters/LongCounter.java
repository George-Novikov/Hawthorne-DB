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
import java.util.concurrent.atomic.AtomicLong;

public class LongCounter extends IdCounter<Long> {
    private StorageArchetype archetype;
    private File counterFile;
    private AtomicLong atomicLong;

    public LongCounter(StorageArchetype archetype) throws IOException, HawthorneException {
        this.archetype = archetype;

        this.counterFile = FileFactory.getInstance().getFile(
                PathBuilder.getIdCounterPath(archetype),
                true
        );

        this.atomicLong = new AtomicLong();
    }

    @Override
    public Long getNext() throws Exception {
        try {
            long idCount = getGenerationsCount();
            atomicLong.set(idCount);

            Long newValue = atomicLong.incrementAndGet();
            updateArchetypePartitionInfo(newValue);
            saveCounterValue(newValue);

            return newValue;
        } catch (Exception e){
            throw new HawthorneException(Message.ID_COUNTER_ERROR, e);
        }
    }

    private void updateArchetypePartitionInfo(long idCount) throws Exception {
        int partitioningThreshold = StorageSettings.getInstance().getPartitioningThreshold();

        int currentPartition = archetype.getPartitionCounter();
        int targetPartition = idCount > currentPartition * partitioningThreshold ? currentPartition + 1 : currentPartition;

        archetype.setPartitionCounter(targetPartition);
    }

    @Override
    public long getGenerationsCount() throws Exception {
        String idCount = FileManager.read(counterFile);
        if (idCount == null || idCount.isEmpty()) idCount = DEFAULT_ID_COUNT_STRING_VALUE;
        return Long.valueOf(idCount);
    }

    private void saveCounterValue(Long newValue) throws Exception {
        FileManager.write(counterFile, String.valueOf(newValue));
    }
}
