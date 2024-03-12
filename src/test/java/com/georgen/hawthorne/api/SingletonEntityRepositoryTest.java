package com.georgen.hawthorne.api;

import com.georgen.hawthorne.api.annotations.SingletonEntity;
import com.georgen.hawthorne.api.model.SingletonEntitySample;
import com.georgen.hawthorne.api.model.SingletonFileSample;
import com.georgen.hawthorne.model.sample.Sample;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.settings.StorageSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingletonEntityRepositoryTest {
    @Test
    public void testSingletonEntitySampleSave(){
        try {
            SingletonEntitySample sample = new SingletonEntitySample();
            sample.setField("How are you?");

            SingletonEntitySample savedSample = Repository.save(sample);
            assertEquals(sample, savedSample); // essentially they are the same object

            SingletonEntitySample retrievedSample = Repository.get(SingletonEntitySample.class);
            assertEquals(sample, retrievedSample);

            List<SingletonEntitySample> sampleList = Repository.list(SingletonEntitySample.class, 0, 0);
            assertEquals(1, sampleList.size());
            assertEquals(sample, sampleList.get(0));

            boolean isDeleted = Repository.delete(SingletonEntitySample.class);
            assertEquals(true, isDeleted);

            long count = Repository.count(SingletonEntitySample.class);
            assertEquals(0, count);

            StorageSchema schema = StorageSettings.getInstance().getStorageSchema();
            schema.unregister(SingletonEntitySample.class);
        } catch (Exception e){
            Assertions.fail("SingletonEntityRepositoryTest failed.",  e);
        }
    }
}
