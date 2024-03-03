package com.georgen.hawthorne.io;

import com.georgen.hawthorne.model.constants.FileExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;

public class FileFilter implements FilenameFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileFilter.class);
    private String extension;
    private int limit;
    private int offset;
    private int counter = 0;

    public FileFilter(FileExtension extension, int limit, int offset){
        this.extension = extension.getValue();
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public boolean accept(File dir, String name) {
        boolean isInRequiredRange = counter > offset && this.counter <= limit + offset;
        boolean hasRequestedExtension = name.endsWith(extension);
        this.counter++;
        LOGGER.info("FileFilter counter value: {}", counter);
        return isInRequiredRange && hasRequestedExtension;
    }
}
