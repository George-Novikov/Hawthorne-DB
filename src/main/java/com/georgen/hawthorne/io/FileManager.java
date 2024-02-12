package com.georgen.hawthorne.io;

import com.georgen.hawthorne.tools.FileSystemHelper;
import com.georgen.hawthorne.tools.OSInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileManager {
    public static void write(File file, String content) throws Exception {
        try (CloseableWriter writer = new CloseableWriter(file, false)){
            writer.append(content);
        }
    }

    public static String read(File file) throws Exception {
        try (CloseableScanner scanner = new CloseableScanner(file)){
            return scanner.read();
        }
    }

    public static void writeBytes(File file, byte[] content) throws IOException {
        Files.write(file.toPath(), content);
    }

    public static byte[] readBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    public static void createOrBypass(File file) throws IOException {
        boolean isCreated = file.createNewFile();

        if (isCreated && OSInfo.isUnixSystem()){
            FileSystemHelper.setFilePermissions(file);
        }
    }
}
