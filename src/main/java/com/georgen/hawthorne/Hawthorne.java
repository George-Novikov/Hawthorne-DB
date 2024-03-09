package com.georgen.hawthorne;

import com.georgen.hawthorne.api.Repository;
import com.georgen.hawthorne.model.sample.Sample;
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
            Sample retrievedSample = Repository.get(Sample.class, "86a0fad8-12a4-4839-a4dd-403824421b71");
            List<Sample> samples = Repository.list(Sample.class, 1, 5);
            boolean isDeleted = Repository.delete(Sample.class, "86a0fad8-12a4-4839-a4dd-403824421b71");

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args){}
}
