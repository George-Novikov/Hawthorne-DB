package com.georgen.hawthorne.io.comparators;

import java.io.File;
import java.util.Comparator;

public class FileIdNameComparator implements Comparator<File> {
    @Override
    public int compare(File file1, File file2) {
        try {
            long firstId = extractFileId(file1);
            long secondId = extractFileId(file2);

            return Long.compare(firstId, secondId);
        } catch (Exception e){
            return file1.getName().compareTo(file2.getName());
        }
    }

    private long extractFileId(File file){
        String fileName = file.getName();
        int start = fileName.lastIndexOf(File.separator) + 1;
        int end = fileName.lastIndexOf('.');
        String idString = fileName.substring(start, end);
        return Long.valueOf(idString);
    }
}
