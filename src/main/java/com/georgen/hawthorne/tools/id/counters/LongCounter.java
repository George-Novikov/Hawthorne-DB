package com.georgen.hawthorne.tools.id.counters;

import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.io.FileIOManager;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.messages.Message;
import com.georgen.hawthorne.model.storage.StorageArchetype;
import com.georgen.hawthorne.tools.paths.PathBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class LongCounter extends IdCounter<Long> {
    private File counterFile;
    private AtomicLong atomicLong;

    public LongCounter(StorageArchetype archetype) throws IOException, HawthorneException {

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
            saveCounterValue(newValue);

            return newValue;
        } catch (Exception e){
            throw new HawthorneException(Message.ID_COUNTER_ERROR, e);
        }
    }

    @Override
    public long getGenerationsCount() throws Exception {
        String idCount = FileIOManager.read(counterFile);
        if (idCount == null || idCount.isEmpty()) idCount = DEFAULT_ID_COUNT_STRING_VALUE;
        return Long.valueOf(idCount);
    }

    private void saveCounterValue(Long newValue) throws Exception {
        FileIOManager.write(counterFile, String.valueOf(newValue));
    }
}
