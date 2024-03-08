package com.georgen.hawthorne.tools.id.counters;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileIOManager;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.tools.paths.PathBuilder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UuidCounter extends IdCounter<String> {
    private File counterFile;
    private AtomicLong atomicLong;

    public UuidCounter(StorageArchetype archetype) throws IOException, HawthorneException {

        this.counterFile = FileFactory.getInstance().getFile(
                PathBuilder.getIdCounterPath(archetype),
                true
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

            saveCounterValue(incrementedCount);

            return nextUuid;
        } catch (Exception e) {
            throw new HawthorneException(Message.ID_COUNTER_ERROR, e);
        }
    }

    @Override
    public long getGenerationsCount() throws Exception {
        String idCount = FileIOManager.read(counterFile);
        if (idCount == null || idCount.isEmpty()) idCount = "0";
        return Long.valueOf(idCount);
    }

    private void saveCounterValue(Long incrementedCount) throws Exception {
        FileIOManager.write(counterFile, String.valueOf(incrementedCount));
    }
}
