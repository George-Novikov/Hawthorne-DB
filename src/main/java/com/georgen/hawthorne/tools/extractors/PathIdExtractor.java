package com.georgen.hawthorne.tools.extractors;

import java.io.File;
import java.nio.file.Path;
import java.util.UUID;

public class PathIdExtractor {
    public static String extractStringId(Path path){
        String pathString = path.toString();
        int start = pathString.lastIndexOf(File.separator) + 1;
        int end = pathString.lastIndexOf('.');
        return pathString.substring(start, end);
    }

    public static boolean isUuidString(String value){
        return value.contains("-") && value.length() == 36 && value.split("-").length == 5;
    }
}
