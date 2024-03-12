package com.georgen.hawthorne.api;

import com.georgen.hawthorne.api.model.SingletonEntitySample;
import com.georgen.hawthorne.api.model.SingletonFileSample;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.settings.StorageSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingletonFileRepositoryTest {
    @Test
    public void testSingletonFileSampleSave(){
        try {
            SingletonFileSample sample = new SingletonFileSample();
            sample.setField("How are you?");
            sample.setBytes("How are you?".getBytes());

            SingletonFileSample savedSample = Repository.save(sample);
            assertEquals(sample, savedSample); // essentially they are the same object

            SingletonFileSample retrievedSample = Repository.get(SingletonFileSample.class);
            assertEquals(sample.getField(), retrievedSample.getField());
            assertArrayEquals(sample.getBytes(), retrievedSample.getBytes());
            assertEquals(sample, retrievedSample);

            List<SingletonFileSample> sampleList = Repository.list(SingletonFileSample.class, 0, 0);
            assertEquals(1, sampleList.size());
            assertEquals(sample, sampleList.get(0));

            boolean isDeleted = Repository.delete(SingletonFileSample.class);
            assertEquals(true, isDeleted);

            long count = Repository.count(SingletonFileSample.class);
            assertEquals(0, count);

            StorageSchema schema = StorageSettings.getInstance().getStorageSchema();
            schema.unregister(SingletonFileSample.class);
        } catch (Exception e){
            Assertions.fail("SingletonFileRepositoryTest failed.",  e);
        }
    }
}
