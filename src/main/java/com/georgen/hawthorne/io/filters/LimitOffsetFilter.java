package com.georgen.hawthorne.io.filters;

import com.georgen.hawthorne.model.constants.FileExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;

public class LimitOffsetFilter implements FilenameFilter {
    private String extension;
    private int limit;
    private int offset;
    private int counter = 0;

    public LimitOffsetFilter(FileExtension extension, int limit, int offset){
        this.extension = extension.getValue();
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public boolean accept(File dir, String name) {
        boolean isInRequiredRange = counter > offset && this.counter <= limit + offset;
        boolean hasRequestedExtension = name.endsWith(extension);
        this.counter++;
        return isInRequiredRange && hasRequestedExtension;
    }
}
