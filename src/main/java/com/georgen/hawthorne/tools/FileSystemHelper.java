package com.georgen.hawthorne.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class FileSystemHelper {
    public static void setFilePermissions(File file) throws IOException {
        Set<PosixFilePermission> permissions = Arrays
                .stream(PosixFilePermission.values())
                .filter(permission -> !permission.name().startsWith("OTHERS"))
                .collect(Collectors.toSet());

        Files.setPosixFilePermissions(file.toPath(), permissions);
    }
}
