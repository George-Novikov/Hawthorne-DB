package com.georgen.hawthorne;

import com.georgen.hawthorne.config.SettingsContainer;
import com.georgen.hawthorne.config.SystemConfig;
import com.georgen.hawthorne.io.FileFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Hawthorne {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hawthorne.class);
    private static final SystemConfig CONFIG = new SystemConfig();

    static {
        try {
            File controlFile = FileFactory.getControlFile();
            LOGGER.info("Control file: {}", controlFile.toPath());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args){}
}
