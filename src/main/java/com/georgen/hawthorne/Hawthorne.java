package com.georgen.hawthorne;

import com.georgen.hawthorne.api.Repository;
import com.georgen.hawthorne.model.constants.IdType;
import com.georgen.hawthorne.model.sample.Sample;
import com.georgen.hawthorne.tools.Serializer;
import com.georgen.hawthorne.tools.extractors.IdTypeExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Hawthorne {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hawthorne.class);

    static {
        try {
            Sample sample = new Sample();
            sample.setBytes("How are you?".getBytes());

            Sample savedSample = Repository.save(sample);
            Sample retrievedSample = Repository.get(Sample.class, 1);
            List<Sample> samples = Repository.list(Sample.class, 0, 0);
            //boolean isDeleted = Repository.delete(Sample.class, 1);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args){}
}
