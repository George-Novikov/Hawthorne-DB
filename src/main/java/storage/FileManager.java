package storage;

import tools.FileSystemHelper;
import tools.OSHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class FileManager {
    public static void write(File file, String content) throws IOException {
        FileWriter writer = new FileWriter(file, false);
        writer.append(content);
        writer.close();
    }

    public static String read(File file) throws Exception {
        try (CloseableScanner scanner = new CloseableScanner(file)){
            return scanner.read();
        }
    }

    public static void createOrBypass(File file) throws IOException {
        boolean isCreated = file.createNewFile();

        if (isCreated && OSHelper.isUnixSystem()){
            FileSystemHelper.setFilePermissions(file);
        }
    }
}
