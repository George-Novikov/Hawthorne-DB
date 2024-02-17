package com.georgen.hawthorne;

import com.georgen.hawthorne.api.annotations.AnnotationProcessor;
import com.georgen.hawthorne.api.repositories.MonoEntityRepository;
import com.georgen.hawthorne.api.repositories.Repository;
import com.georgen.hawthorne.io.FileFactory;
import com.georgen.hawthorne.model.sample.Sample;
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

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args){}
}
