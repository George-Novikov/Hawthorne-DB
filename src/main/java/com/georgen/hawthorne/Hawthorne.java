package com.georgen.hawthorne;

import com.georgen.hawthorne.config.SystemConfig;
import com.georgen.hawthorne.model.constants.ConfigPropertyName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Hawthorne {
    private static final Logger LOGGER = LoggerFactory.getLogger(Hawthorne.class);
    private static final SystemConfig CONFIG = new SystemConfig();

    static {
        try {
            LOGGER.info("Main file name: {}", CONFIG.getProperty(ConfigPropertyName.MANAGING_FILE_NAME));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args){

    }
}
