package com.georgen.hawthorne;

import com.georgen.hawthorne.api.repositories.Repository;
import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.sample.Sample;
import com.georgen.hawthorne.tools.extractors.IdTypeExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class Hawthorne {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hawthorne.class);

    static {
        try {
            File controlFile = FileFactory.getControlFile();
            LOGGER.info("Control file: {}", controlFile.toPath());

            Sample sample = new Sample("This is a long message to test bytes serialization");
            Sample savedSample = Repository.save(sample);
            LOGGER.info("Saved sample is not null: {}", savedSample != null);

            Sample retrievedSample = Repository.get(Sample.class);
            LOGGER.info("Retrieved sample is not null: {}", retrievedSample != null);
            LOGGER.info("Sample field: {}", sample.getField());

            IdType idType = IdTypeExtractor.extract(retrievedSample);
            LOGGER.info("Retrieved sample IdType: {}", idType);

            List<Sample> sampleList = Repository.list(Sample.class);
            LOGGER.info("Sample list: {}", sampleList);

            boolean isDeleted = Repository.delete(Sample.class);
            LOGGER.info("Is sample deleted: {}", isDeleted);

            long sampleCount = Repository.count(Sample.class);
            LOGGER.info("Sample count: {}", sampleCount);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args){}
}
