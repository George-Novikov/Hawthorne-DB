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
import java.util.concurrent.atomic.AtomicInteger;

public class IntegerCounter extends IdCounter<Integer> {
    private StorageArchetype archetype;
    private File counterFile;
    private AtomicInteger atomicInteger;

    public IntegerCounter(StorageArchetype archetype) throws IOException, HawthorneException {
        this.archetype = archetype;

        this.counterFile = FileFactory.getInstance().getFile(
                PathBuilder.getIdCounterPath(archetype),
                true
        );

        this.atomicInteger = new AtomicInteger();
    }

    @Override
    public Integer getNext() throws Exception {
        try {
            long idCount = getGenerationsCount();
            atomicInteger.set(Math.toIntExact(idCount));

            Integer newValue = atomicInteger.incrementAndGet();
            updateArchetypePartitionInfo(newValue);
            saveCounterValue(newValue);

            return newValue;
        } catch (Exception e){
            throw new HawthorneException(Message.ID_COUNTER_ERROR, e);
        }
    }

    @Override
    public long getGenerationsCount() throws Exception {
        String idCount = FileManager.read(counterFile);
        if (idCount == null || idCount.isEmpty()) idCount = DEFAULT_ID_COUNT_STRING_VALUE;
        return Long.valueOf(idCount);
    }

    private void updateArchetypePartitionInfo(Integer idCount) throws Exception {
        int partitioningThreshold = StorageSettings.getInstance().getPartitioningThreshold();

        int currentPartition = archetype.getPartitionCounter();
        int targetPartition = idCount > currentPartition * partitioningThreshold ? currentPartition + 1 : currentPartition;

        archetype.setPartitionCounter(targetPartition);
    }

    private void saveCounterValue(Integer newValue) throws Exception {
        FileManager.write(counterFile, String.valueOf(newValue));
    }
}
