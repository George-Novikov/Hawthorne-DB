package com.georgen.hawthorne.io;

import com.georgen.hawthorne.config.Settings;
import com.georgen.hawthorne.tools.SystemHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class FileFactory {
    private static File controlFile;

    public static File getControlFile(){
        if (controlFile == null){
            synchronized (FileFactory.class){
                if (controlFile == null){
                    controlFile = Settings.getInstance().getControlFile();
                }
            }
        }
        return controlFile;
    }

    public static File getFile(String name) throws IOException {
        File file = new File(name);
        if (!file.exists()) file = createFile(file);
        return file;
    }

    private static File createFile(File file) throws IOException {
        file.getParentFile().mkdirs();
        file.createNewFile();
        setFilePermissions(file);
        return file;
    }

    private static void setFilePermissions(File file) throws IOException {
        if (!SystemHelper.isUnixSystem()) return;

        Set<PosixFilePermission> permissions = Arrays
                .stream(PosixFilePermission.values())
                .filter(permission -> !permission.name().startsWith("OTHERS"))
                .collect(Collectors.toSet());

        Files.setPosixFilePermissions(file.toPath(), permissions);
    }
}
