package com.georgen.hawthorne.api;

import com.georgen.hawthorne.api.model.EntityCollectionSample;
import com.georgen.hawthorne.api.model.FileCollectionSample;
import com.georgen.hawthorne.model.exceptions.HawthorneException;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.settings.StorageSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileCollectionRepositoryTest {
    @Test
    public void testFileCollectionSampleTest(){
        try {
            FileCollectionSample sample = new FileCollectionSample();
            sample.setField("How are you?");
            sample.setBytes("How are you?".getBytes());

            /**
             * Simple save operation ("sample" and "savedSample" are essentially the same object)
             */
            FileCollectionSample savedSample = Repository.save(sample);
            assertEquals(sample.getField(), savedSample.getField());
            assertEquals(sample.getUuid(), savedSample.getUuid());
            assertArrayEquals(sample.getBytes(), savedSample.getBytes());
            assertEquals(sample, savedSample);

            String uuid = savedSample.getUuid();

            /**
             * Throws HawthorneException
             */
            HawthorneException exception = assertThrows(
                    HawthorneException.class,
                    () -> Repository.get(FileCollectionSample.class)
            );

            /**
             * Simple get operation
             */
            FileCollectionSample retrievedSample = Repository.get(FileCollectionSample.class, uuid);
            assertEquals(sample, retrievedSample);

            /**
             * Repeated save operation with data changes
             */
            FileCollectionSample anotherSample = new FileCollectionSample();
            anotherSample.setUuid(savedSample.getUuid());
            anotherSample.setField("This is another field text.");
            anotherSample.setBytes("This is another binary data content.".getBytes());
            FileCollectionSample reSavedSample = Repository.save(anotherSample);
            assertNotEquals(sample.getField(), reSavedSample.getField());
            assertNotEquals(sample.getBytes(), reSavedSample.getBytes());

            /**
             * Bulk save operation
             */
            List<String> uuids = new ArrayList<>();
            uuids.add(uuid);

            for (int i = 0; i < 4; i++){
                FileCollectionSample newSample = new FileCollectionSample();
                newSample.setField("How are you?");
                newSample.setBytes("How are you?".getBytes());
                Repository.save(newSample);
                uuids.add(newSample.getUuid());
            }

            /**
             * List operation
             */
            List<FileCollectionSample> sampleList = Repository.list(FileCollectionSample.class, 3, 2);
            assertNotNull(sampleList);
            assertNotNull(sampleList.get(0));
            assertEquals(3, sampleList.size());

            /**
             * Bulk delete operation
             */
            for (int i = 1; i < 6; i++){
                String deletingUuid = uuids.get(i - 1);
                boolean isDeleted = Repository.delete(FileCollectionSample.class, deletingUuid);
                assertEquals(true, isDeleted);
            }

            /**
             * Cleanup
             */
            StorageSchema schema = StorageSettings.getInstance().getStorageSchema();
            schema.unregister(FileCollectionSample.class);
        } catch (Exception e){
            Assertions.fail("FileCollectionRepositoryTest failed.",  e);
        }
    }
}
