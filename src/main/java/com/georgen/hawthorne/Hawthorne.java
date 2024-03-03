package com.georgen.hawthorne;

import com.georgen.hawthorne.api.Repository;
import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.sample.Sample;
import com.georgen.hawthorne.tools.Serializer;
import com.georgen.hawthorne.tools.PathBuilder;
import com.georgen.hawthorne.tools.extractors.IdTypeExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Hawthorne {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hawthorne.class);

    static {
        try {

            List<Sample> samples = new ArrayList<>();
            for (int i = 0; i < 10; i++){
                samples.add(new Sample("This is a long message to test bytes serialization"));

                Sample savedSample = Repository.save(samples.get(i));
                LOGGER.info("Saved sample is not null: {}", savedSample != null);
                LOGGER.info("Saved sample id: {}", savedSample.getId());
            }

            Sample retrievedSample = Repository.get(Sample.class, 3);
            LOGGER.info("Retrieved sample is not null: {}", retrievedSample != null);
            if (retrievedSample != null){
                LOGGER.info("Sample field: {}", retrievedSample.getField());
                IdType idType = IdTypeExtractor.extract(retrievedSample);
                LOGGER.info("Retrieved sample IdType: {}", idType);
            }

            List<Sample> sampleList = Repository.list(Sample.class, 5, 3);
            for (Sample sampleElement : sampleList){
                LOGGER.info("Sample list element: {}", Serializer.toJson(sampleElement));
            }

            boolean isDeleted = Repository.delete(Sample.class, 3);
            LOGGER.info("Is sample deleted: {}", isDeleted);

            long sampleCount = Repository.count(Sample.class);
            LOGGER.info("Sample count: {}", sampleCount);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args){}
}
