package com.georgen.hawthorne;

import com.georgen.hawthorne.api.repositories.Repository;
import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.sample.Sample;
import com.georgen.hawthorne.tools.extractors.IdTypeExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Hawthorne {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hawthorne.class);

    static {
        try {
            File controlFile = FileFactory.getControlFile();
            LOGGER.info("Control file: {}", controlFile.toPath());

            Sample sample = new Sample("This is a long message to test bytes serialization");
            File file = Repository.save(sample);
            LOGGER.info("File is not null: {}", file != null);

            Sample retrievedSample = Repository.get(Sample.class);
            LOGGER.info("Retrieved sample is not null: {}", retrievedSample != null);
            LOGGER.info("Sample field: {}", sample.getField());

            IdType idType = IdTypeExtractor.extract(retrievedSample);
            LOGGER.info("Retrieved sample IdType: {}", idType);

            boolean isDeleted = Repository.delete(Sample.class);
            LOGGER.info("Is sample deleted: {}", isDeleted);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args){}
}
