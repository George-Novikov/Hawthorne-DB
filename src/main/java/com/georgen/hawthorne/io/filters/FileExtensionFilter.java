package com.georgen.hawthorne.io.filters;

import com.georgen.hawthorne.model.constants.FileExtension;

import java.io.File;
import java.io.FilenameFilter;

public class FileExtensionFilter implements FilenameFilter {
    private String extension;

    public FileExtensionFilter(FileExtension extension){
        this.extension = extension.getValue();
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(extension);
    }
}
