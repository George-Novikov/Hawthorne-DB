package com.georgen.hawthorne.io;

import com.georgen.hawthorne.config.SettingsContainer;
import com.georgen.hawthorne.config.SystemConfig;
import com.georgen.hawthorne.model.exceptions.InitializationException;
import com.georgen.hawthorne.tools.OSInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class FileFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileFactory.class);
    private static File controlFile;

    public static File getControlFile(){
        if (controlFile == null){
            synchronized (FileFactory.class){
                if (controlFile == null){
                    initControlFile();
                }
            }
        }
        return controlFile;
    }

    public static File getFile(String name) throws IOException {
        return createFile(name);
    }

    private static File createFile(String path) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdirs();
        setFilePermissions(file);
        return file;
    }

    public static void setFilePermissions(File file) throws IOException {
        if (!OSInfo.isUnixSystem()) return;

        Set<PosixFilePermission> permissions = Arrays
                .stream(PosixFilePermission.values())
                .filter(permission -> !permission.name().startsWith("OTHERS"))
                .collect(Collectors.toSet());

        Files.setPosixFilePermissions(file.toPath(), permissions);
    }

    private static void initControlFile() {
        try {
            SystemConfig config = SettingsContainer.getInstance().getConfig();
            controlFile = createFile(config.getControlFilePath());
            setFilePermissions(controlFile);
        } catch (IOException e) {
            throw new InitializationException(e);
        }
    }
}
