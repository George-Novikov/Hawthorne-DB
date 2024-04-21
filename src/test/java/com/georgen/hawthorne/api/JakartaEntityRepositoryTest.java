package com.georgen.hawthorne.api;

import com.georgen.hawthorne.api.model.EntityCollectionSample;
import com.georgen.hawthorne.api.model.JakartaEntitySample;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.settings.StorageSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JakartaEntityRepositoryTest {
    @Test
    public void testJakartaEntitySave(){
        try {
            JakartaEntitySample sample = new JakartaEntitySample();
            sample.setField("Jakarta");
            sample.setDate(LocalDateTime.now());

            /**
             * Simple save operation ("sample" and "savedSample" are essentially the same object)
             */
            JakartaEntitySample savedSample = Repository.save(sample);
            assertEquals(sample.getField(), savedSample.getField());
            assertEquals(sample.getId(), savedSample.getId());
            assertEquals(sample.getDate(), savedSample.getDate());
            assertEquals(sample, savedSample);

            /**
             * Throws HawthorneException
             */
            HawthorneException exception = assertThrows(
                    HawthorneException.class,
                    () -> Repository.get(JakartaEntitySample.class)
            );

            /**
             * Simple get operation
             */
            JakartaEntitySample retrievedSample = Repository.get(JakartaEntitySample.class, 1);
            assertEquals(sample, retrievedSample);

            /**
             * Repeated save operation with data changes
             */
            JakartaEntitySample anotherSample = new JakartaEntitySample();
            anotherSample.setId(savedSample.getId());
            anotherSample.setField("This is another Jakarta text.");
            anotherSample.setDate(LocalDateTime.now());
            JakartaEntitySample reSavedSample = Repository.save(anotherSample);
            assertNotEquals(sample.getField(), reSavedSample.getField());

            /**
             * Bulk save operation
             */
            for (int i = 0; i < 4; i++){
                JakartaEntitySample newSample = new JakartaEntitySample();
                newSample.setField("Jakarta");
                sample.setDate(LocalDateTime.now());
                Repository.save(newSample);
            }

            /**
             * List operation
             */
            List<JakartaEntitySample> sampleList = Repository.list(JakartaEntitySample.class, 3, 2);
            assertNotNull(sampleList);
            assertNotNull(sampleList.get(0));
            assertEquals(3, sampleList.size());

            /**
             * Bulk delete operation
             */
            for (int i = 1; i < 6; i++){
                boolean isDeleted = Repository.delete(JakartaEntitySample.class, i);
                assertEquals(true, isDeleted);
            }

            /**
             * Cleanup
             */
            StorageSchema schema = StorageSettings.getInstance().getStorageSchema();
            schema.unregister(JakartaEntitySample.class);
        } catch (Exception e){
            Assertions.fail("JakartaEntityRepositoryTest failed.",  e);
        }
    }
}
