package com.georgen.hawthorne.tools.id.counters;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileManager;
import com.georgen.hawthorne.model.constants.SystemProperty;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.tools.PathBuilder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UuidCounter extends IdCounter<String> {
    private File counterFile;
    private AtomicLong atomicLong;

    public UuidCounter(StorageArchetype archetype) throws IOException {
        this.counterFile = FileFactory.getFile(
                PathBuilder.getIdCounterPath(archetype)
        );
        this.atomicLong = new AtomicLong();
    }

    @Override
    public String getNext() throws Exception {
        try {
            String nextValue = UUID.randomUUID().toString();

            String idCount = FileManager.read(counterFile);
            atomicLong.set(Long.valueOf(idCount));
            Long incrementedLong = atomicLong.incrementAndGet();
            FileManager.write(counterFile, String.valueOf(incrementedLong));

            return nextValue;
        } catch (Exception e) {
            throw new HawthorneException(Message.ID_COUNTER_ERROR);
        }
    }

    public long getGenerationsCount() throws Exception {
        String idCount = FileManager.read(counterFile);
        return Long.valueOf(idCount);
    }
}
