package com.georgen.hawthorne.api;

import com.georgen.hawthorne.api.model.EntityCollectionSample;
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

            /**
             * Simple save operation ("sample" and "savedSample" are essentially the same object)
             */
            EntityCollectionSample savedSample = Repository.save(sample);
            assertEquals(sample.getField(), savedSample.getField());
            assertEquals(sample.getId(), savedSample.getId());
            assertEquals(sample, savedSample);

            /**
             * Throws HawthorneException
             */
            HawthorneException exception = assertThrows(
                    HawthorneException.class,
                    () -> Repository.get(EntityCollectionSample.class)
            );

            /**
             * Simple get operation
             */
            EntityCollectionSample retrievedSample = Repository.get(EntityCollectionSample.class, 1);
            assertEquals(sample, retrievedSample);

            /**
             * Repeated save operation with data changes
             */
            EntityCollectionSample anotherSample = new EntityCollectionSample();
            anotherSample.setId(savedSample.getId());
            anotherSample.setField("This is another field text.");
            EntityCollectionSample reSavedSample = Repository.save(anotherSample);
            assertNotEquals(sample.getField(), reSavedSample.getField());

            /**
             * Bulk save operation
             */
            for (int i = 0; i < 4; i++){
                EntityCollectionSample newSample = new EntityCollectionSample();
                newSample.setField("How are you?");
                Repository.save(newSample);
            }

            /**
             * List operation
             */
            List<EntityCollectionSample> sampleList = Repository.list(EntityCollectionSample.class, 3, 2);
            assertNotNull(sampleList);
            assertNotNull(sampleList.get(0));
            assertEquals(3, sampleList.size());

            /**
             * Bulk delete operation
             */
            for (int i = 1; i < 6; i++){
                boolean isDeleted = Repository.delete(EntityCollectionSample.class, i);
                assertEquals(true, isDeleted);
            }

            /**
             * Cleanup
             */
            StorageSchema schema = StorageSettings.getInstance().getStorageSchema();
            schema.unregister(EntityCollectionSample.class);
        } catch (Exception e){
            Assertions.fail("EntityCollectionRepositoryTest failed.",  e);
        }
    }
}
