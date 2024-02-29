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
import java.util.concurrent.atomic.AtomicInteger;

public class IntegerCounter extends IdCounter<Integer> {
    private static final String DEFAULT_VALUE = "0";
    private File counterFile;
    private AtomicInteger atomicInteger;

    public IntegerCounter(StorageArchetype archetype) throws IOException {
        this.counterFile = FileFactory.getFile(
                PathBuilder.getIdCounterPath(archetype)
        );
        this.atomicInteger = new AtomicInteger();
    }

    @Override
    public Integer getNext() throws Exception {
        try {
            String value = FileManager.read(counterFile);
            if (value == null) value = DEFAULT_VALUE;
            atomicInteger.set(Integer.valueOf(value));

            Integer nextValue = atomicInteger.incrementAndGet();
            FileManager.write(counterFile, String.valueOf(nextValue));

            return nextValue;
        } catch (Exception e){
            throw new HawthorneException(Message.ID_COUNTER_ERROR);
        }
    }

    @Override
    public long getGenerationsCount() throws Exception {
        return 0;
    }
}
