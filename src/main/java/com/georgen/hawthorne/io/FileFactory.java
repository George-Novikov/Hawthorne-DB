package com.georgen.hawthorne.io;

import com.georgen.hawthorne.config.SystemConfig;
import com.georgen.hawthorne.model.exceptions.InitializationException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class FileFactory {
    private static final SystemConfig CONFIG = new SystemConfig();
    private static File managingFile;

    static {
        try {
            managingFile = new File(CONFIG.getManagingFilePath());
            setFilePermissions(managingFile);
        } catch (IOException e) {
            throw new InitializationException(e);
        }
    }



    public static File getFile(String name) throws IOException {
        return createFile(name);
    }

    private static File createFile(String path) throws IOException {
        File file = new File(path);
        setFilePermissions(file);
        return file;
    }

    public static void setFilePermissions(File file) throws IOException {
        Set<PosixFilePermission> permissions = Arrays
                .stream(PosixFilePermission.values())
                .filter(permission -> !permission.name().startsWith("OTHERS"))
                .collect(Collectors.toSet());

        Files.setPosixFilePermissions(file.toPath(), permissions);
    }
}
