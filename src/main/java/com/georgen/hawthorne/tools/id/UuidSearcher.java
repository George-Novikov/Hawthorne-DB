package com.georgen.hawthorne.tools.id;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class UuidSearcher {

    public static boolean isUuidPresent(File uuidIndexFile, String uuid) throws IOException {
        List<String> lines = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(uuidIndexFile));
            lines = reader.lines().collect(Collectors.toList());
            Collections.sort(lines);
            return Collections.binarySearch(lines, uuid) >= 0;
        } finally {
            if (reader != null) reader.close();
        }
    }

    public static int binarySearch(List<String> uuidIndex, String uuid){
        int leftCursor = 0;
        int rightCursor = uuidIndex.size() - 1;

        while (leftCursor <= rightCursor){
            int pointer = (leftCursor + rightCursor) / 2;

            String sample = uuidIndex.get(pointer);
            int result = sample.compareTo(uuid);

            if (result < 0){
                leftCursor = pointer + 1;
            } else if (result > 0){
                rightCursor = pointer - 1;
            } else {
                return pointer;
            }
        }

        return -1;
    }
}
