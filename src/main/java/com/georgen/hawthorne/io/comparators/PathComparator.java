package com.georgen.hawthorne.io.comparators;

import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;

public class PathComparator implements Comparator<Path> {
    @Override
    public int compare(Path path1, Path path2) {
        long fileId1 = extractFileId(path1);
        long fileId2 = extractFileId(path2);
        return Long.compare(fileId1, fileId2);
    }

    protected long extractFileId(Path path){
        String pathString = path.toString();
        int start = pathString.lastIndexOf(File.separator) + 1;
        int end = pathString.lastIndexOf('.');
        String idString = pathString.substring(start, end);
        return Long.valueOf(idString);
    }
}
