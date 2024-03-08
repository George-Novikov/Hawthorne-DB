package com.georgen.hawthorne.tools.paths;

import com.georgen.hawthorne.tools.extractors.PathIdExtractor;

import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.UUID;

public class PathComparator implements Comparator<Path> {
    @Override
    public int compare(Path path1, Path path2) {
        String stringId1 = PathIdExtractor.extractStringId(path1);
        String stringId2 = PathIdExtractor.extractStringId(path2);
        boolean isUuid = PathIdExtractor.isUuidString(stringId1);
        return isUuid ? compareUuid(stringId1, stringId2) : compareLong(stringId1, stringId2);
    }

    private int compareLong(String stringId1, String stringId2){
        long id1 = Long.valueOf(stringId1);
        long id2 = Long.valueOf(stringId2);
        return Long.compare(id1, id2);
    }

    private int compareUuid(String stringId1, String stringId2){
        UUID uuid1 = UUID.fromString(stringId1);
        UUID uuid2 = UUID.fromString(stringId2);
        return uuid1.compareTo(uuid2);
    }

    public long extractFileId(Path path){
        String pathString = path.toString();
        int start = pathString.lastIndexOf(File.separator) + 1;
        int end = pathString.lastIndexOf('.');
        String idString = pathString.substring(start, end);
        return Long.valueOf(idString);
    }
}
