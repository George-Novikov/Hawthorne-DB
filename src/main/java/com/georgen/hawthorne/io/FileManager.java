package com.georgen.hawthorne.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileManager {
    public static void write(File file, String content) throws Exception {
        synchronized (file){
            try (CloseableWriter writer = new CloseableWriter(file, false)){
                writer.append(content);
            }
        }
    }

    public static String read(File file) throws Exception {
        synchronized (file){
            try (CloseableScanner scanner = new CloseableScanner(file)){
                return scanner.read();
            }
        }
    }

    public static void writeBytes(File file, byte[] content) throws IOException {
        synchronized (file){
            Files.write(file.toPath(), content);
        }
    }

    public static byte[] readBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }
}
