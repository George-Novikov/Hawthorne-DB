package com.georgen.hawthorne.api;

import com.georgen.hawthorne.api.model.EntityCollectionSample;
import com.georgen.hawthorne.api.model.SingletonEntitySample;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.settings.StorageSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityCollectionRepositoryTest {
    @Test
    public void testEntityCollectionSampleSave(){
        try {
            EntityCollectionSample sample = new EntityCollectionSample();
            sample.setField("How are you?");

            EntityCollectionSample savedSample = Repository.save(sample);
            // essentially they are the same object
            assertEquals(sample.getField(), savedSample.getField());
            assertEquals(sample.getId(), savedSample.getId());
            assertEquals(sample, savedSample);

            HawthorneException exception = assertThrows(
                    HawthorneException.class,
                    () -> Repository.get(EntityCollectionSample.class)
            );

            EntityCollectionSample retrievedSample = Repository.get(EntityCollectionSample.class, 1);
            assertEquals(sample, retrievedSample);

            for (int i = 0; i < 4; i++){
                EntityCollectionSample newSample = new EntityCollectionSample();
                newSample.setField("How are you?");
                Repository.save(newSample);
            }

            List<EntityCollectionSample> sampleList = Repository.list(EntityCollectionSample.class, 3, 2);
            assertNotNull(sampleList);
            assertNotNull(sampleList.get(0));
            assertEquals(3, sampleList.size());

            for (int i = 1; i < 6; i++){
                boolean isDeleted = Repository.delete(EntityCollectionSample.class, i);
                assertEquals(true, isDeleted);
            }

            StorageSchema schema = StorageSettings.getInstance().getStorageSchema();
            schema.unregister(EntityCollectionSample.class);
        } catch (Exception e){
            Assertions.fail("EntityCollectionRepositoryTest failed.",  e);
        }
    }
}
