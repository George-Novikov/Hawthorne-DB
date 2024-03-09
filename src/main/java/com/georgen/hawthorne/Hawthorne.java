package com.georgen.hawthorne;

import com.georgen.hawthorne.api.Repository;
import com.georgen.hawthorne.model.sample.Sample;
import com.georgen.hawthorne.model.storage.StorageSchema;
import com.georgen.hawthorne.settings.StorageSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Hawthorne {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hawthorne.class);

    static {
        try {
            Sample sample = new Sample();
            sample.setBytes("How are you?".getBytes());

            Sample savedSample = Repository.save(sample);
            Sample retrievedSample = Repository.get(Sample.class);
            List<Sample> samples = Repository.list(Sample.class, 0, 0);
            for (Sample element : samples){
                LOGGER.info(new String(element.getBytes()));
            }
            boolean isDeleted = Repository.delete(Sample.class);

            StorageSchema schema = StorageSettings.getInstance().getStorageSchema();
            schema.unregister(Sample.class);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args){}
}
